package hr.fer.help193.vehicle.app.features.interventions.ui

import hr.fer.help193.share.model.rest.Intervention
import hr.fer.help193.vehicle.app.base.BasePresenter
import hr.fer.help193.vehicle.app.features.interventions.usecase.QueryAllActiveInterventions
import hr.fer.help193.vehicle.core.navigation.router.main.MainRouter
import hr.fer.help193.vehicle.utils.toFormattedDate
import hr.fer.help193.vehicle.utils.toFormattedTime
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.processors.BehaviorProcessor

class InterventionsPresenter(private val queryAllActiveInterventions: QueryAllActiveInterventions)
    : BasePresenter<InterventionsContract.View, InterventionsViewState>(), InterventionsContract.Presenter {

    private val selectedInterventionPublisher = BehaviorProcessor.create<Int>()
    private val interventions: MutableList<Intervention> = mutableListOf()

    override fun onSelectedChanged(interventionId: Int) {
        if (selectedInterventionPublisher.hasValue()) {
            if (interventionId == selectedInterventionPublisher.value!!) {
                selectedInterventionPublisher.onNext(-1)
            } else {
                selectedInterventionPublisher.onNext(interventionId)
            }
        } else {
            selectedInterventionPublisher.onNext(interventionId)
        }
    }

    override fun initialViewState(): InterventionsViewState = InterventionsViewState(emptyList())

    override fun onStart() {
        query(Flowable.combineLatest(
                queryAllActiveInterventions(),
                selectedInterventionPublisher.observeOn(backgroundScheduler).startWith(-1),
                BiFunction(this::toViewState)))
    }

    override fun exit() = dispatchRoutingAction(MainRouter::goBack)

    override fun chooseIntervention() {
        if (selectedInterventionPublisher.hasValue()) {
            dispatchRoutingAction { it.showAllVehicles(selectedInterventionPublisher.value!!) }
        }
    }

    private fun toViewState(allInterventions: List<Intervention>, selectedInterventionId: Int): (InterventionsViewState) -> Unit =
            { viewState: InterventionsViewState ->
                interventions.clear()
                interventions.addAll(allInterventions)
                viewState.interventions = toInterventionViewModels(interventions.sortedBy(Intervention::alarmTimestamp), selectedInterventionId).apply {
                    if (isNotEmpty()) else listOf(EmptyInterventionListViewModel(-1))
                }
            }

    private fun toInterventionViewModels(allInterventions: List<Intervention>, selectedInterventionId: Int): List<InterventionsViewModel> =
            allInterventions.map { toInterventionViewModel(it, selectedInterventionId) }

    private fun toInterventionViewModel(intervention: Intervention, selectedInterventionId: Int): InterventionsViewModel =
            with(intervention) {
                InterventionViewModel(
                        id!!,
                        toFormattedDate(alarmTimestamp!!),
                        toFormattedTime(alarmTimestamp!!),
                        eventType,
                        districtOffice ?: "Nedefinirano",
                        neighborhood ?: "Nedefinirano",
                        street.plus(", ").plus(houseNumber),
                        description ?: "Nedefinirano",
                        selectedInterventionId == id!!
                )
            }
}
