package hr.fer.help193.vehicle.app.features.popup.di

import hr.fer.help193.vehicle.app.di.BACKGROUND_SCHEDULER
import hr.fer.help193.vehicle.app.di.MAIN_SCHEDULER
import hr.fer.help193.vehicle.app.features.popup.PopupMessage
import hr.fer.help193.vehicle.app.features.popup.ui.PopupMessageContract
import hr.fer.help193.vehicle.app.features.popup.ui.PopupMessagePresenter
import hr.fer.help193.vehicle.core.navigation.MAIN_ROUTER_DISPATCHER
import org.koin.core.qualifier.named
import org.koin.dsl.module

val PopupMessageModule = module {

    scope(named(POPUP_MESSAGE_VIEW_SCOPE)) {

        scoped<PopupMessageContract.Presenter> {
            val popupMessage: PopupMessage = it[0]
            PopupMessagePresenter(popupMessage).apply {
                mainThreadScheduler = get(named(MAIN_SCHEDULER))
                backgroundScheduler = get(named(BACKGROUND_SCHEDULER))
                routingActionsDispatcher = get(named(MAIN_ROUTER_DISPATCHER))
                start()
            }
        }
    }

}

const val POPUP_MESSAGE_VIEW_SCOPE = "Popup message view scope"
