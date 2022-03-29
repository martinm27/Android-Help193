package hr.fer.help193.vehicle

import android.app.Application
import android.util.Log
import com.mapbox.mapboxsdk.Mapbox
import hr.fer.help193.vehicle.app.activity.main.MainActivityModule
import hr.fer.help193.vehicle.app.activity.onboarding.OnboardingActivityModule
import hr.fer.help193.vehicle.app.di.AppModule
import hr.fer.help193.vehicle.app.di.ThreadingModule
import hr.fer.help193.vehicle.app.features.connection.di.ConnectivityBarModule
import hr.fer.help193.vehicle.app.features.interventiondetails.di.InterventionDetailsModule
import hr.fer.help193.vehicle.app.features.interventions.di.InterventionsModule
import hr.fer.help193.vehicle.app.features.kiosk.di.KioskModule
import hr.fer.help193.vehicle.app.features.map.di.MapModule
import hr.fer.help193.vehicle.app.features.onboarding.di.OnboardingModule
import hr.fer.help193.vehicle.app.features.permissionerror.di.PermissionErrorModule
import hr.fer.help193.vehicle.app.features.popup.di.PopupMessageModule
import hr.fer.help193.vehicle.app.features.vehicles.di.VehiclesModule
import hr.fer.help193.vehicle.core.navigation.NavigationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class Help193Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Mapbox.getInstance(applicationContext, getString(R.string.mapbox_access_token))
        Timber.plant(DebugTree())

        startKoin {
            androidContext(this@Help193Application)
            modules(listOf(
                    AppModule,
                    ThreadingModule,
                    NavigationModule,
                    OnboardingActivityModule,
                    MainActivityModule,
                    ConnectivityBarModule,
                    PermissionErrorModule,
                    MapModule,
                    OnboardingModule,
                    KioskModule,
                    InterventionsModule,
                    VehiclesModule,
                    PopupMessageModule,
                    InterventionDetailsModule))
        }
    }

    private class DebugTree : Timber.DebugTree() {

        override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
            if (priority == Log.ERROR) {
                throw throwable ?: RuntimeException(message)
            }

            super.log(priority, tag, message, throwable)
        }

        override fun createStackElementTag(element: StackTraceElement) = "(${element.fileName}:${element.lineNumber})#${element.methodName} DebugTree"
    }
}
