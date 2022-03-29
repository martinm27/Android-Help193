package hr.fer.help193.vehicle.app.features.vehicles.ui

import hr.fer.help193.share.model.rest.FireStation
import hr.fer.help193.share.model.rest.Vehicle
import hr.fer.help193.vehicle.app.base.BasePresenter
import hr.fer.help193.vehicle.app.features.map.usecase.ChooseIntervention
import hr.fer.help193.vehicle.app.features.vehicles.usecase.QueryAllFireStations
import hr.fer.help193.vehicle.app.features.vehicles.usecase.QueryAllVehicles
import hr.fer.help193.vehicle.core.navigation.router.main.MainRouter
import io.reactivex.Flowable
import io.reactivex.functions.Function3
import io.reactivex.processors.BehaviorProcessor

class VehiclesPresenter(private val interventionId: Int,
                        private val queryAllVehicles: QueryAllVehicles,
                        private val queryAllFireStations: QueryAllFireStations,
                        private val chooseIntervention: ChooseIntervention) : BasePresenter<VehiclesContract.View, VehiclesViewState>(), VehiclesContract.Presenter {

    private val selectedVehiclePublisher = BehaviorProcessor.create<Int>()
    private val vehicles: MutableList<Vehicle> = mutableListOf()

    override fun onSelectedChanged(vehicleId: Int) {
        if (selectedVehiclePublisher.hasValue()) {
            if (vehicleId == selectedVehiclePublisher.value!!) {
                selectedVehiclePublisher.onNext(-1)
            } else {
                selectedVehiclePublisher.onNext(vehicleId)
            }
        } else {
            selectedVehiclePublisher.onNext(vehicleId)
        }
    }


    override fun initialViewState(): VehiclesViewState = VehiclesViewState(emptyMap())

    override fun onStart() {
        query(Flowable.combineLatest(
                queryAllVehicles(),
                queryAllFireStations(),
                selectedVehiclePublisher.observeOn(backgroundScheduler).startWith(-1),
                Function3(this::toViewState)
        ))
    }

    override fun chooseVehicle() {
        if (selectedVehiclePublisher.hasValue()) {
            runCommand(chooseIntervention(ChooseIntervention.Request(interventionId, selectedVehiclePublisher.value!!)))
            dispatchRoutingAction(MainRouter::returnToMap)
        }
    }

    override fun returnToInterventions() = dispatchRoutingAction(MainRouter::goBack)

    private fun toViewState(allVehicles: List<Vehicle>, allFireStations: List<FireStation>, selectedVehicleId: Int): (VehiclesViewState) -> Unit =
            { viewState: VehiclesViewState ->
                vehicles.clear()
                vehicles.addAll(allVehicles)
                viewState.vehicles = toStationWithVehicleViewModel(allFireStations, vehicles, selectedVehicleId)
            }

    private fun toStationWithVehicleViewModel(allFireStations: List<FireStation>, allVehicles: List<Vehicle>, selectedVehicleId: Int): Map<StationViewModel, List<VehicleViewModel>> =
            allFireStations.asSequence()
                    .map { station -> toStationViewModel(station) to toVehicleViewModels(allVehicles.filter { vehicle -> vehicle.fireStationId == station.id }, selectedVehicleId, station.color) }
                    .toMap()

    private fun toVehicleViewModels(vehicles: List<Vehicle>, selectedVehicleId: Int, stationColor: String): List<VehicleViewModel> =
            vehicles.map { toVehicleViewModel(it, selectedVehicleId, stationColor) }

    private fun toVehicleViewModel(vehicle: Vehicle, selectedVehicleId: Int, stationColor: String): VehicleViewModel = VehicleViewModel(vehicle.id!!, vehicle.gricId, selectedVehicleId == vehicle.id!!, stationColor)

    private fun toStationViewModel(station: FireStation): StationViewModel = StationViewModel(station.id!!, station.name)

}
