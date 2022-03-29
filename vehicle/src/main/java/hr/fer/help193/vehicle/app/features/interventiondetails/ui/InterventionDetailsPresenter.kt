package hr.fer.help193.vehicle.app.features.interventiondetails.ui

import hr.fer.help193.share.model.rest.Intervention
import hr.fer.help193.vehicle.app.base.BasePresenter
import hr.fer.help193.vehicle.core.interventiontracker.InterventionTracker
import hr.fer.help193.vehicle.core.navigation.router.main.MainRouter

class InterventionDetailsPresenter(private val intervetionTracker: InterventionTracker) : BasePresenter<InterventionDetailsContract.View, InterventionDetailsViewState>(), InterventionDetailsContract.Presenter {

    override fun initialViewState() = InterventionDetailsViewState(Intervention.EMPTY)

    override fun onStart() {
        query(intervetionTracker.intervention()
                .map(this::toViewState))
    }

    private fun toViewState(intervention: Intervention?): (InterventionDetailsViewState) -> Unit =
            { viewState: InterventionDetailsViewState ->
                viewState.intervention = intervention ?: Intervention.EMPTY
            }

    override fun returnToMap() {
        dispatchRoutingAction(MainRouter::returnToMap)
    }
}
