package hr.fer.help193.vehicle.app.features.interventions.di

import android.view.LayoutInflater
import hr.fer.help193.vehicle.app.di.BACKGROUND_SCHEDULER
import hr.fer.help193.vehicle.app.di.MAIN_SCHEDULER
import hr.fer.help193.vehicle.app.features.interventions.ui.InterventionsAdapter
import hr.fer.help193.vehicle.app.features.interventions.ui.InterventionsContract
import hr.fer.help193.vehicle.app.features.interventions.ui.InterventionsPresenter
import hr.fer.help193.vehicle.app.features.interventions.ui.OnSelectedChanged
import hr.fer.help193.vehicle.app.features.interventions.usecase.QueryAllActiveInterventions
import hr.fer.help193.vehicle.core.navigation.MAIN_ROUTER_DISPATCHER
import org.koin.core.qualifier.named
import org.koin.dsl.module

val InterventionsModule = module {

    scope(named(INTERVENTIONS_SCOPE)) {

        scoped<InterventionsContract.Presenter> {
            InterventionsPresenter(get()).apply {
                mainThreadScheduler = get(named(MAIN_SCHEDULER))
                backgroundScheduler = get(named(BACKGROUND_SCHEDULER))
                routingActionsDispatcher = get(named(MAIN_ROUTER_DISPATCHER))
                start()
            }
        }

        factory {
            val layoutInflater: LayoutInflater = it[0]
            val onSelectedChanged: OnSelectedChanged = it[1]
            InterventionsAdapter(layoutInflater, onSelectedChanged)
        }

        single { QueryAllActiveInterventions(get()) }
    }
}

const val INTERVENTIONS_SCOPE = "Interventions scope"
