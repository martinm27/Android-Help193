package hr.fer.help193.vehicle.app.activity.main

import hr.fer.help193.vehicle.core.connection.ConnectionManager
import hr.fer.help193.vehicle.core.connection.ConnectionManagerImpl
import hr.fer.help193.vehicle.data.datasource.connection.WebSocketConnectionDatasource
import hr.fer.help193.vehicle.data.datasource.connection.WebSocketConnectionDatasourceImpl
import hr.fer.help193.vehicle.data.datasource.restapi.RestApiDatasource
import hr.fer.help193.vehicle.data.datasource.restapi.RestApiDatasourceImpl
import hr.fer.help193.vehicle.data.datasource.websocket.WebSocketDatasource
import hr.fer.help193.vehicle.data.datasource.websocket.WebSocketDatasourceImpl
import hr.fer.help193.vehicle.data.network.CommandCenterDataService
import hr.fer.help193.vehicle.data.network.WebSocketInterventionService
import hr.fer.help193.vehicle.app.di.BACKGROUND_SCHEDULER
import hr.fer.help193.vehicle.app.di.createOkHttpWithBasicInterceptor
import hr.fer.help193.vehicle.app.di.createRestService
import hr.fer.help193.vehicle.app.di.createWebSocketService
import hr.fer.help193.vehicle.data.service.GpsService
import hr.fer.help193.vehicle.data.service.GpsServiceImpl
import hr.fer.help193.vehicle.utils.KEY_AUTHENTICATION_PASSWORD
import hr.fer.help193.vehicle.utils.KEY_AUTHENTICATION_USERNAME
import hr.fer.help193.vehicle.utils.KEY_TABLET_ID
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val SERVER_URL = "https://applygit.zvne.fer.hr/vatrogasci/be/"
private const val WEB_SOCKET_INTERVENTION_URL = "wss://applygit.zvne.fer.hr/vatrogasci/be/ws/tablet/"

val MainActivityModule = module {

    scope(named(MAIN_ACTIVITY_SCOPE)) {
            single {
                createRestService<CommandCenterDataService>(
                        createOkHttpWithBasicInterceptor(getProperty(KEY_AUTHENTICATION_USERNAME), getProperty(KEY_AUTHENTICATION_PASSWORD)),
                        SERVER_URL,
                        get()
                )
            }

        single<ConnectionManager> { ConnectionManagerImpl(get(), get(named(BACKGROUND_SCHEDULER))) }
        single<WebSocketDatasource> { WebSocketDatasourceImpl(get(), get()) }
        single<WebSocketConnectionDatasource> { WebSocketConnectionDatasourceImpl(get()) }
        single<RestApiDatasource> { RestApiDatasourceImpl(get()) }
        single {
            val username = getProperty(KEY_AUTHENTICATION_USERNAME) as String
            val password = getProperty(KEY_AUTHENTICATION_PASSWORD) as String
            val tabletId = getProperty(KEY_TABLET_ID) as Int

            val interventionURL = WEB_SOCKET_INTERVENTION_URL.plus(tabletId)
            createWebSocketService<WebSocketInterventionService>(
                    createOkHttpWithBasicInterceptor(username, password),
                    interventionURL,
                    get(),
                    androidApplication()
            )
        }
        single<GpsService> { GpsServiceImpl(get(), get()) }
    }
}

const val MAIN_ACTIVITY_SCOPE = "Main activity scope"
