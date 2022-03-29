package hr.fer.help193.vehicle.app.features.interventiondetails.ui

import hr.fer.help193.vehicle.app.base.BaseView
import hr.fer.help193.vehicle.app.base.ViewPresenter

interface InterventionDetailsContract {

    interface View : BaseView

    interface Presenter : ViewPresenter<View, InterventionDetailsViewState> {

        fun returnToMap()
    }
}
