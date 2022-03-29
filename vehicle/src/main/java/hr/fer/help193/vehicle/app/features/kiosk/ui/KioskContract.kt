package hr.fer.help193.vehicle.app.features.kiosk.ui

import hr.fer.help193.vehicle.app.base.BaseView
import hr.fer.help193.vehicle.app.base.ViewPresenter

interface KioskContract {

    interface View : BaseView

    interface Presenter : ViewPresenter<View, KioskViewState> {

        fun startNavigation()

        fun leaveApp()
    }
}
