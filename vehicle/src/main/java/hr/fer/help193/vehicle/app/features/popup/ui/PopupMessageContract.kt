package hr.fer.help193.vehicle.app.features.popup.ui

import hr.fer.help193.vehicle.app.base.BaseView
import hr.fer.help193.vehicle.app.base.ViewPresenter

interface PopupMessageContract {

    interface View : BaseView

    interface Presenter : ViewPresenter<View, PopupMessageViewState> {

        fun executeAction(action: () -> Unit)
    }
}
