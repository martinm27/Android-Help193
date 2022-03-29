package hr.fer.help193.vehicle.app.features.kiosk.di

import hr.fer.help193.vehicle.app.di.BACKGROUND_SCHEDULER
import hr.fer.help193.vehicle.app.di.MAIN_SCHEDULER
import hr.fer.help193.vehicle.app.features.kiosk.ui.KioskContract
import hr.fer.help193.vehicle.app.features.kiosk.ui.KioskPresenter
import hr.fer.help193.vehicle.core.navigation.MAIN_ROUTER_DISPATCHER
import org.koin.core.qualifier.named
import org.koin.dsl.module

val KioskModule = module {

    scope(named(KIOSK_SCOPE)) {

        scoped<KioskContract.Presenter>  {
            KioskPresenter().apply {
                mainThreadScheduler = get(named(MAIN_SCHEDULER))
                backgroundScheduler = get(named(BACKGROUND_SCHEDULER))
                routingActionsDispatcher = get(named(MAIN_ROUTER_DISPATCHER))
                start()
            }
        }
    }
}

const val KIOSK_SCOPE = "Kiosk scope"
