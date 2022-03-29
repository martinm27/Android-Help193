package hr.fer.help193.vehicle.app.features.connection.di

import hr.fer.help193.vehicle.app.di.BACKGROUND_SCHEDULER
import hr.fer.help193.vehicle.app.di.MAIN_SCHEDULER
import hr.fer.help193.vehicle.app.features.connection.resources.ConnectivityStatusResources
import hr.fer.help193.vehicle.app.features.connection.resources.ConnectivityStatusResourcesImpl
import hr.fer.help193.vehicle.app.features.connection.ui.ConnectivityBarContract
import hr.fer.help193.vehicle.app.features.connection.ui.ConnectivityBarPresenter
import hr.fer.help193.vehicle.app.features.connection.usecase.QueryIsServerReadyWithDelay
import hr.fer.help193.vehicle.core.navigation.MAIN_ROUTER_DISPATCHER
import org.koin.core.qualifier.named
import org.koin.dsl.module

val ConnectivityBarModule = module {

    scope(named(CONNECTIVITY_BAR_SCOPE)) {

        scoped<ConnectivityBarContract.Presenter> {
            ConnectivityBarPresenter(get(), get()).apply {
                mainThreadScheduler = get(named(MAIN_SCHEDULER))
                backgroundScheduler = get(named(BACKGROUND_SCHEDULER))
                routingActionsDispatcher = get(named(MAIN_ROUTER_DISPATCHER))
                start()
            }
        }

        single<ConnectivityStatusResources> { ConnectivityStatusResourcesImpl(get()) }
        single { QueryIsServerReadyWithDelay(get()) }
    }
}

const val CONNECTIVITY_BAR_SCOPE = "Connectivity Bar Scope"
