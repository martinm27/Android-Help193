package hr.fer.help193.vehicle.app.features.connection.ui

import hr.fer.help193.vehicle.app.base.BaseView
import hr.fer.help193.vehicle.app.base.ViewPresenter

interface ConnectivityBarContract {

    interface View : BaseView

    interface Presenter : ViewPresenter<View, ConnectivityBarViewState>
}
