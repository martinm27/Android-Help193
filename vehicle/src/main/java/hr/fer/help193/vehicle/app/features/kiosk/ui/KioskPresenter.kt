package hr.fer.help193.vehicle.app.features.kiosk.ui

import hr.fer.help193.vehicle.app.base.BasePresenter
import hr.fer.help193.vehicle.core.navigation.router.main.MainRouter

class KioskPresenter : BasePresenter<KioskContract.View, KioskViewState>(), KioskContract.Presenter {


    override fun initialViewState(): KioskViewState = KioskViewState()

    override fun onStart() {
    }

    override fun startNavigation() = dispatchRoutingAction(MainRouter::startNavigation)

    override fun leaveApp() = dispatchRoutingAction(MainRouter::leaveApp)
}

