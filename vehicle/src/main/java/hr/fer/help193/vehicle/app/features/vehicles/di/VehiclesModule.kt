package hr.fer.help193.vehicle.app.features.vehicles.di

import android.view.LayoutInflater
import hr.fer.help193.vehicle.app.di.BACKGROUND_SCHEDULER
import hr.fer.help193.vehicle.app.di.MAIN_SCHEDULER
import hr.fer.help193.vehicle.app.features.map.usecase.ChooseIntervention
import hr.fer.help193.vehicle.app.features.vehicles.ui.OnSelectedChanged
import hr.fer.help193.vehicle.app.features.vehicles.ui.VehiclesAdapter
import hr.fer.help193.vehicle.app.features.vehicles.ui.VehiclesContract
import hr.fer.help193.vehicle.app.features.vehicles.ui.VehiclesPresenter
import hr.fer.help193.vehicle.app.features.vehicles.usecase.QueryAllFireStations
import hr.fer.help193.vehicle.app.features.vehicles.usecase.QueryAllVehicles
import hr.fer.help193.vehicle.core.navigation.MAIN_ROUTER_DISPATCHER
import org.koin.core.qualifier.named
import org.koin.dsl.module

val VehiclesModule = module {

    scope(named(VEHICLES_SCOPE)) {

        scoped<VehiclesContract.Presenter> {
            val interventionId: Int = it[0]
            VehiclesPresenter(interventionId, get(), get(), get()).apply {
                mainThreadScheduler = get(named(MAIN_SCHEDULER))
                backgroundScheduler = get(named(BACKGROUND_SCHEDULER))
                routingActionsDispatcher = get(named(MAIN_ROUTER_DISPATCHER))
                start()
            }
        }

        factory {
            val layoutInflater: LayoutInflater = it[0]
            val onSelectedChanged: OnSelectedChanged = it[1]
            VehiclesAdapter(layoutInflater, onSelectedChanged)
        }

        single { QueryAllVehicles(get()) }
        single { QueryAllFireStations(get()) }
        single { ChooseIntervention(get()) }
    }
}

const val VEHICLES_SCOPE = "Vehicles scope"
