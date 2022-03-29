package hr.fer.help193.vehicle.data.datasource.restapi

import hr.fer.help193.share.model.rest.*
import hr.fer.help193.vehicle.core.extension.shareReplayLatest
import hr.fer.help193.vehicle.data.network.CommandCenterDataService
import io.reactivex.Flowable
import timber.log.Timber

class RestApiDatasourceImpl(private val commandCenterDataService: CommandCenterDataService)
    : RestApiDatasource {

    private val firestations = commandCenterDataService.getFirestations()
            .doOnNext { Timber.i("Dispatching REST data service firestations: > $it") }
            .doOnError { Timber.w("Error occurred > $it") }
            .distinctUntilChanged()
            .shareReplayLatest()

    private val interventions = commandCenterDataService.getInterventions()
            .doOnNext { Timber.i("Dispatching REST data service intervention: > $it") }
            .doOnError { Timber.w("Error occurred > $it") }
            .distinctUntilChanged()
            .shareReplayLatest()

    private val vehicles = commandCenterDataService.getVehicles()
            .doOnNext { Timber.i("Dispatching REST data service vehicle: > $it") }
            .doOnError { Timber.w("Error occurred > $it") }
            .distinctUntilChanged()
            .shareReplayLatest()

    override fun tablets(tabletId: Int): Flowable<Tablet> = commandCenterDataService.getTabletData(tabletId)
            .doOnNext { Timber.i("Dispatching REST data service tablets: > $it") }
            .doOnError { Timber.w("Error occurred > $it") }
            .distinctUntilChanged()
            .shareReplayLatest()

    override fun firestations(): Flowable<List<FireStation>> = firestations

    override fun hydrants(x: Double, y: Double, distance: Double): Flowable<List<Hydrant>> = commandCenterDataService.getHydrants(x, y, distance)
            .doOnNext { Timber.i("Dispatching REST data service hydrants: > $it") }
            .doOnError { Timber.w("Error occurred > $it") }
            .distinctUntilChanged()
            .shareReplayLatest()

    override fun routeDescriptions(documentedAreaId: Int, firestationId: Int): Flowable<RouteDescription> = commandCenterDataService.getRouteDescriptions(documentedAreaId, firestationId)
            .doOnNext { Timber.i("Dispatching REST data service route descriptions: > $it") }
            .doOnError { Timber.w("Error occurred > $it") }
            .distinctUntilChanged()
            .shareReplayLatest()

    override fun interventions(): Flowable<List<Intervention>> = interventions

    override fun vehicles(): Flowable<List<Vehicle>> = vehicles
}
