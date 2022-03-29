package hr.fer.help193.vehicle.app.features.vehicles.ui

import hr.fer.help193.vehicle.app.base.BaseView
import hr.fer.help193.vehicle.app.base.ViewPresenter

interface VehiclesContract {

    interface View : BaseView

    interface Presenter : ViewPresenter<View, VehiclesViewState> {

        fun onSelectedChanged(vehicleId: Int)

        fun returnToInterventions()

        fun chooseVehicle()
    }
}
