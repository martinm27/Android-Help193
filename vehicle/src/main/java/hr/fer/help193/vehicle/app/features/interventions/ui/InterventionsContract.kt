package hr.fer.help193.vehicle.app.features.interventions.ui

import hr.fer.help193.vehicle.app.base.BaseView
import hr.fer.help193.vehicle.app.base.ViewPresenter

interface InterventionsContract {

    interface View : BaseView

    interface Presenter : ViewPresenter<View, InterventionsViewState> {

        fun onSelectedChanged(interventionId: Int)

        fun exit()

        fun chooseIntervention()
    }
}
