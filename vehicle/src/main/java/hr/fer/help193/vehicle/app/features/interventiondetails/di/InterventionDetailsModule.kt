package hr.fer.help193.vehicle.app.features.interventiondetails.di

import hr.fer.help193.vehicle.app.di.BACKGROUND_SCHEDULER
import hr.fer.help193.vehicle.app.di.MAIN_SCHEDULER
import hr.fer.help193.vehicle.app.features.interventiondetails.ui.InterventionDetailsContract
import hr.fer.help193.vehicle.app.features.interventiondetails.ui.InterventionDetailsPresenter
import hr.fer.help193.vehicle.core.navigation.MAIN_ROUTER_DISPATCHER
import org.koin.core.qualifier.named
import org.koin.dsl.module

val InterventionDetailsModule = module {

    scope(named(INTERVENTION_DETAILS_SCOPE)) {

        scoped<InterventionDetailsContract.Presenter> {
            InterventionDetailsPresenter(get()).apply {
                mainThreadScheduler = get(named(MAIN_SCHEDULER))
                backgroundScheduler = get(named(BACKGROUND_SCHEDULER))
                routingActionsDispatcher = get(named(MAIN_ROUTER_DISPATCHER))
                start()
            }
        }
    }
}

const val INTERVENTION_DETAILS_SCOPE = "INTERVENTION_DETAILS_SCOPE"
