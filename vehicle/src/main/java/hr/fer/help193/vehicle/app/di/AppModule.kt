package hr.fer.help193.vehicle.app.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.fasterxml.jackson.databind.ObjectMapper
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.jackson.JacksonMessageAdapter
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
import com.tinder.scarlet.retry.LinearBackoffStrategy
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.ShutdownReason
import com.tinder.scarlet.websocket.okhttp.OkHttpWebSocket
import hr.fer.help193.share.config.applyCustomConfiguration
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.permission.PermissionManagement
import hr.fer.help193.vehicle.app.permission.PermissionManagementImpl
import hr.fer.help193.vehicle.core.interventiontracker.InterventionTracker
import hr.fer.help193.vehicle.core.interventiontracker.InterventionTrackerImpl
import hr.fer.help193.vehicle.core.vehicletracker.VehicleTracker
import hr.fer.help193.vehicle.core.vehicletracker.VehicleTrackerImpl
import hr.fer.help193.vehicle.data.network.BasicAuthInterceptor
import hr.fer.help193.vehicle.core.navigation.router.main.MainRouter
import hr.fer.help193.vehicle.core.navigation.router.main.MainRouterImpl
import hr.fer.help193.vehicle.core.navigation.router.onboarding.OnboardingRouter
import hr.fer.help193.vehicle.core.navigation.router.onboarding.OnboardingRouterImpl
import hr.fer.help193.vehicle.utils.SharedPrefsUtils
import hr.fer.help193.vehicle.utils.SharedPrefsUtilsImpl
import hr.fer.help193.vehicle.utils.get
import hr.fer.help193.vehicle.app.view.GridItemSpacingDecoration
import hr.fer.help193.vehicle.app.view.VerticalItemDecoration
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

val AppModule = module {

    factory<MainRouter> {
        val activity: FragmentActivity = it[0]
        val fragmentManager: FragmentManager = activity.supportFragmentManager
        MainRouterImpl(activity, fragmentManager)
    }

    factory<OnboardingRouter> {
        val activity: FragmentActivity = it[0]
        val fragmentManager: FragmentManager = activity.supportFragmentManager
        OnboardingRouterImpl(activity, fragmentManager)
    }

    single<PermissionManagement> {
        val activity: FragmentActivity = it[0]
        PermissionManagementImpl(activity, get())
    }

    factory {
        val context: Context = it[0]
        val spanCount: Int = context.resources.getInteger(it.get(1, R.integer.interventions_grid_count))
        GridLayoutManager(context, spanCount)
    }

    factory {
        val context: Context = it[0]
        GridItemSpacingDecoration(context, R.dimen.common_gutter_size)
    }

    factory {
        val context: Context = it[0]
        val itemOffset: Int = context.resources.getDimension(R.dimen.common_vertical_item_padding).toInt()
        VerticalItemDecoration(itemOffset)
    }

    single<Resources> { get<Application>().resources }

    single<SharedPrefsUtils> { SharedPrefsUtilsImpl(androidContext()) }
    single<InterventionTracker> { InterventionTrackerImpl(get(named(BACKGROUND_SCHEDULER))) }
    single<VehicleTracker> { VehicleTrackerImpl(get(named(BACKGROUND_SCHEDULER))) }
}

fun createOkHttpWithBasicInterceptor(username: String, password: String): OkHttpClient {
    return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(BasicAuthInterceptor(username, password))
            .build()
}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
}

inline fun <reified T> createRestService(okHttpClient: OkHttpClient, url: String, objectMapper: ObjectMapper): T {
    val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}

inline fun <reified T> createWebSocketService(okHttpClient: OkHttpClient, url: String, objectMapper: ObjectMapper, application: Application): T {
    val protocol = OkHttpWebSocket(
            okHttpClient,
            OkHttpWebSocket.SimpleRequestFactory(
                    { Request.Builder().url(url).build() },
                    { ShutdownReason.GRACEFUL }
            )
    )
    val configuration = Scarlet.Configuration(
            messageAdapterFactories = listOf(JacksonMessageAdapter.Factory(objectMapper)),
            streamAdapterFactories = listOf(RxJava2StreamAdapterFactory()),
            backoffStrategy = LinearBackoffStrategy(5000),
            lifecycle = AndroidLifecycle.ofApplicationForeground(application)
    )
    return Scarlet(protocol, configuration).create(T::class.java)
}


