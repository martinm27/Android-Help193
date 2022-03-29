package hr.fer.help193.vehicle.app.features.map.ui

import android.graphics.Color
import hr.fer.help193.share.model.rest.*
import hr.fer.help193.share.model.websocket.vehicle.MessageToTablet
import hr.fer.help193.vehicle.app.base.BasePresenter
import hr.fer.help193.vehicle.app.features.map.translations.MapTranslations
import hr.fer.help193.vehicle.app.features.map.usecase.*
import hr.fer.help193.vehicle.app.features.popup.PopupMessage
import hr.fer.help193.vehicle.app.features.popup.PopupMessageButton
import hr.fer.help193.vehicle.app.features.vehicles.usecase.QueryAllFireStations
import hr.fer.help193.vehicle.core.HYDRANTS_DISTANCE_PERIMETER
import hr.fer.help193.vehicle.core.LOCATION_DISTANCE_TOLERANCE
import hr.fer.help193.vehicle.core.ZAGREB_LATITUDE
import hr.fer.help193.vehicle.core.ZAGREB_LONGITUDE
import hr.fer.help193.vehicle.core.interventiontracker.InterventionTracker
import hr.fer.help193.vehicle.core.navigation.router.main.MainRouter
import hr.fer.help193.vehicle.core.vehicletracker.VehicleTracker
import hr.fer.help193.vehicle.utils.SharedPrefsUtils
import io.reactivex.Flowable
import io.reactivex.Flowable.combineLatest
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.processors.BehaviorProcessor
import org.locationtech.jts.geom.Point

class MapPresenter(private val queryNewInterventions: QueryNewInterventions,
                   private val mapTranslations: MapTranslations,
                   private val interventionTracker: InterventionTracker,
                   private val vehicleTracker: VehicleTracker,
                   private val queryAllFireStations: QueryAllFireStations,
                   private val translations: MapTranslations,
                   private val queryHydrants: QueryHydrants,
                   private val updatePosition: UpdatePosition,
                   private val sharedPrefsUtils: SharedPrefsUtils,
                   private val queryRouteDescription: QueryRouteDescription,
                   private val queryTabletData: QueryTabletData)
    : BasePresenter<MapContract.View, MapViewState>(), MapContract.Presenter {

    private val positionUpdatePublisher = BehaviorProcessor.create<Point>()

    override fun initialViewState(): MapViewState =
            MapViewState(false, translations.emptyDescription(), translations.emptyVehicleName(), ZAGREB_LATITUDE, ZAGREB_LONGITUDE, emptyList(), translations.fireIcon(), Color.parseColor("#000000"))

    override fun onStart() {
        query(combineLatest(
                interventionTracker.intervention().switchMap(this::getInterventionData),
                vehicleTracker.vehicle(),
                queryAllFireStations(),
                Function3(this::toViewState)
        ))

        addDisposable(queryNewInterventions()
                .subscribeOn(backgroundScheduler)
                .subscribe { toNewIntervention(it) })
    }

    private fun getInterventionData(intervention: Intervention): Flowable<InterventionData> =
            if (intervention !== Intervention.EMPTY) {
                combineLatest(
                        queryHydrants(QueryHydrants.Request(intervention.location?.x ?: ZAGREB_LONGITUDE, intervention.location?.y
                                ?: ZAGREB_LATITUDE, HYDRANTS_DISTANCE_PERIMETER)),
                        queryTabletData(sharedPrefsUtils.getTabletId()).switchMap { tablet ->
                            queryRouteDescription(QueryRouteDescription.Request(intervention.documentedAreaId, tablet.fireStationId))
                        },
                        BiFunction<List<Hydrant>, RouteDescription, InterventionData> { hydrants, routeDescription ->
                            InterventionData(intervention, hydrants, routeDescription)
                        }
                )
            } else {
                Flowable.just(InterventionData(intervention, emptyList(), null))
            }


    private fun toViewState(interventionData: InterventionData?, vehicle: Vehicle?, firestations: List<FireStation>): (MapViewState) -> Unit =
            { viewState: MapViewState ->
                viewState.isInterventionActive = interventionData?.intervention?.isActive ?: false
                viewState.description = interventionData?.intervention?.description ?: translations.emptyDescription()
                viewState.vehicleName = if (vehicle !== Vehicle.EMPTY) vehicle?.gricId
                        ?: translations.emptyVehicleName() else translations.emptyVehicleName()
                viewState.vehicleNameColor = if (vehicle !== Vehicle.EMPTY) Color.parseColor(firestations.find { it.id == vehicle?.fireStationId }?.color
                        ?: "#000000") else Color.parseColor("#000000")
                viewState.latitude = interventionData?.intervention?.location?.y ?: ZAGREB_LATITUDE
                viewState.longitude = interventionData?.intervention?.location?.x ?: ZAGREB_LONGITUDE
                viewState.mapHydrants = interventionData?.hydrants ?: emptyList()
                viewState.description = interventionData?.routeDescription?.text ?: "Nema novih intervencija"
            }

    private fun toNewIntervention(incomingAssignment: MessageToTablet.IncomingAssignment) {
        dispatchRoutingAction { mainRouter ->
            mainRouter.showPopupMessage(
                    PopupMessage(
                            if (incomingAssignment.intervention == null) mapTranslations.interventionClosed() else mapTranslations.newInterventionReady(incomingAssignment.intervention?.event
                                    ?: ""),
                            PopupMessageButton.RegularPopupMessageButton(mapTranslations.cancel()) {
                                if (incomingAssignment.intervention == null) {
                                    interventionTracker.publishNewIntervention(Intervention.EMPTY)
                                    vehicleTracker.publishNewVehicle(Vehicle.EMPTY)
                                }
                                mainRouter.closePopup()
                            },
                            PopupMessageButton.BoldPopupMessageButton(mapTranslations.proceed()) {
                                interventionTracker.publishNewIntervention(if (incomingAssignment.intervention == null) Intervention.EMPTY else incomingAssignment.intervention)
                                vehicleTracker.publishNewVehicle(if (incomingAssignment.vehicle == null) Vehicle.EMPTY else incomingAssignment.vehicle)
                                mainRouter.closePopup()
                            }
                    )
            )
        }
    }

    override fun chooseIntervention() {
        dispatchRoutingAction { mainRouter -> mainRouter.showAllInterventions() }
    }

    override fun returnToKiosk() {
        dispatchRoutingAction(MainRouter::returnToKiosk)
    }

    override fun removeIntervention() {
        dispatchRoutingAction { mainRouter ->
            mainRouter.showPopupMessage(
                    PopupMessage(
                            mapTranslations.closingInterventionWarning(),
                            PopupMessageButton.RegularPopupMessageButton(mapTranslations.cancel()) { mainRouter.closePopup() },
                            PopupMessageButton.BoldPopupMessageButton(mapTranslations.proceed()) {
                                interventionTracker.publishNewIntervention(Intervention.EMPTY)
                                vehicleTracker.publishNewVehicle(Vehicle.EMPTY)
                                mainRouter.closePopup()
                            }
                    )
            )
        }
    }

    override fun showDetails() {
        dispatchRoutingAction(MainRouter::showInterventionDetails)
    }

    override fun updatePosition(point: Point, speed: Double, heading: Double) {
        if (!positionUpdatePublisher.hasValue()) {
            positionUpdatePublisher.onNext(point)
        }
        positionUpdatePublisher.value?.let {
            it.normalize()
            point.normalize()
            if (!it.equalsExact(point, LOCATION_DISTANCE_TOLERANCE)) {
                runCommand(updatePosition(UpdatePosition.Request(point, speed, heading))
                        .doOnComplete { positionUpdatePublisher.onNext(point) })
            }
        }
    }
}
