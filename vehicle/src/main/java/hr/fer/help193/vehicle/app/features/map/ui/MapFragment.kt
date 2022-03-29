package hr.fer.help193.vehicle.app.features.map.ui

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineResult
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager
import com.mapbox.turf.TurfConstants
import com.mapbox.turf.TurfMeasurement
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.base.BaseFragment
import hr.fer.help193.vehicle.app.features.map.di.MAP_SCOPE
import hr.fer.help193.vehicle.app.features.map.location.MapLocationManager
import hr.fer.help193.vehicle.app.features.map.map.MapDecorator
import hr.fer.help193.vehicle.app.features.map.map.MapMarkerManager
import hr.fer.help193.vehicle.app.features.map.map.OfflineMapManager
import hr.fer.help193.vehicle.core.ZAGREB_LATITUDE
import hr.fer.help193.vehicle.core.ZAGREB_LONGITUDE
import hr.fer.help193.vehicle.utils.fadeIn
import hr.fer.help193.vehicle.utils.fadeOut
import hr.fer.help193.vehicle.utils.point
import io.reactivex.processors.BehaviorProcessor
import kotlinx.android.synthetic.main.fragment_map.*
import timber.log.Timber
import java.lang.ref.WeakReference

private const val MAX_ZOOM_SIZE = 16.0

class MapFragment : BaseFragment<MapViewState>(), MapContract.View {

    companion object {
        const val LAYOUT = R.layout.fragment_map
        const val TAG: String = "MapFragment"

        fun newInstance() = MapFragment()
    }

    private val presenter: MapContract.Presenter by scopedInject()
    private lateinit var mapboxMap: MapboxMap
    private lateinit var callback: MapFragmentLocationCallback

    private lateinit var mapLocationManager: MapLocationManager
    private lateinit var mapDecorator: MapDecorator
    private lateinit var mapMarkerManager: MapMarkerManager
    private lateinit var offlineMapManager: OfflineMapManager

    private val autopilotPublisher = BehaviorProcessor.createDefault(false)

    private var currentVehicleLocationLatitude: Double = ZAGREB_LATITUDE
    private var currentVehicleLocationLongitude: Double = ZAGREB_LONGITUDE
    private var currentInterventionLatitude: Double = ZAGREB_LATITUDE
    private var currentInterventionLongitude: Double = ZAGREB_LONGITUDE

    private var interventionActive: Boolean = false
    private var areHydrantsSwitched = false

    override fun getLayoutResource(): Int = LAYOUT
    override fun getScopeName(): String = MAP_SCOPE
    override fun getViewPresenter() = presenter

    @SuppressLint("InflateParams")
    override fun initialiseView(view: View, savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        back_button.setOnLongClickListener {
            presenter.returnToKiosk()
            true
        }
        choose_intervention.setOnClickListener { presenter.chooseIntervention() }
        end_intervention.setOnLongClickListener {
            presenter.removeIntervention()
            autopilotPublisher.onNext(false)
            true
        }

        autopilot.setOnClickListener { autopilotPublisher.onNext(autopilotPublisher.value!!.not()) }
        intervention_details.setOnClickListener { presenter.showDetails() }

        addDisposable(autopilotPublisher.subscribe(this@MapFragment::setAutopilotMode))
        initMap()
    }

    private fun initMap() {
        mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            callback = MapFragmentLocationCallback(this)
            mapLocationManager = MapLocationManager(mapboxMap.locationComponent, context!!, callback)
            mapDecorator = MapDecorator(mapboxMap, context!!)
            mapMarkerManager = MapMarkerManager(MarkerViewManager(mapView, mapboxMap), context!!)
            offlineMapManager = OfflineMapManager(context!!, resources)

            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                offlineMapManager.downloadOfflineMap(it.url)
                mapDecorator.init()
                mapMarkerManager.init()
                mapLocationManager.enableLocationComponent(it)
            }

            mapboxMap.addOnCameraMoveListener {
                if (!areHydrantsSwitched) {
                    if (mapboxMap.cameraPosition.zoom >= MAX_ZOOM_SIZE) {
                        mapDecorator.addHydrants(mapMarkerManager.getHydrants(), currentInterventionLongitude, currentInterventionLatitude)
                    } else {
                        mapDecorator.clearHydrants(mapMarkerManager.getHydrants())
                    }
                }
            }

            mapboxMap.addOnMoveListener(
                    object : MapboxMap.OnMoveListener {
                        override fun onMove(detector: MoveGestureDetector) {}

                        override fun onMoveEnd(detector: MoveGestureDetector) {}

                        override fun onMoveBegin(detector: MoveGestureDetector) {
                            autopilotPublisher.onNext(false)
                        }
                    }
            )
        }
    }

    override fun render(viewState: MapViewState) {
        with(viewState) {
            route_description.text = description
            interventionActive = isInterventionActive

            if (::mapMarkerManager.isInitialized) {
                mapMarkerManager.updateVehicleDecoration(vehicleName, vehicleNameColor)
                mapMarkerManager.updateIntervention(latitude, longitude)
                mapMarkerManager.updateHydrants(mapHydrants)
            }

            if (interventionActive) {
                if (::mapDecorator.isInitialized) {
                    mapDecorator.setCurrentIntervention(latitude, longitude)
                }
                currentInterventionLatitude = latitude
                currentInterventionLongitude = longitude

                intervention_activeMode.fadeIn()
                bottom_divider.fadeIn()

                autopilotPublisher.onNext(true)
                updateMapCamera()
            } else {
                intervention_activeMode.fadeOut()
                bottom_divider.fadeOut()
                autopilotPublisher.onNext(false)
                if (::mapDecorator.isInitialized) {
                    mapDecorator.clear()
                    mapDecorator.clearHydrants(mapMarkerManager.getHydrants())
                }
                if (::mapMarkerManager.isInitialized) {
                    mapMarkerManager.clearHydrants()
                }
            }
        }
    }

    private fun setAutopilotMode(autopilotOn: Boolean) {
        if (autopilotOn) {
            (autopilot.background as GradientDrawable).setColor(ContextCompat.getColor(context!!, R.color.primary_orange_color))
            autopilot.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        } else {
            (autopilot.background as GradientDrawable).setColor(ContextCompat.getColor(context!!, R.color.white))
            (autopilot.background as GradientDrawable).setStroke(5, ContextCompat.getColor(context!!, R.color.primary_orange_color))
            autopilot.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        }
    }

    private fun updateMapCamera() {
        mapDecorator.animateToPosition(currentVehicleLocationLatitude, currentVehicleLocationLongitude)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroyView() {
        mapView.onDestroy()
        mapLocationManager.removeLocationUpdates()
        mapMarkerManager.onDestroy()
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    private class MapFragmentLocationCallback internal constructor(mapFragment: MapFragment) : LocationEngineCallback<LocationEngineResult> {

        private val fragmentWeakReference: WeakReference<MapFragment> = WeakReference(mapFragment)
        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        override fun onSuccess(result: LocationEngineResult) {
            val fragment = fragmentWeakReference.get()

            if (fragment != null) {
                val location = result.lastLocation ?: return
                fragment.currentVehicleLocationLatitude = location.latitude
                fragment.currentVehicleLocationLongitude = location.longitude

                updateMap(location, fragment)
            }
        }

        private fun updateMap(location: Location, fragment: MapFragment) {
            fragment.mapMarkerManager.updateVehicle(location.latitude, location.longitude)

            if (fragment.interventionActive) {
                fragment.mapMarkerManager.updateIntervention(fragment.currentInterventionLatitude, fragment.currentInterventionLongitude)

                if (TurfMeasurement.distance(Point.fromLngLat(location.longitude, location.latitude), Point.fromLngLat(fragment.currentInterventionLongitude, fragment.currentInterventionLatitude), TurfConstants.UNIT_METERS) <= 500.0) {
                    if (!fragment.areHydrantsSwitched) {
                        fragment.mapDecorator.clearHydrants(fragment.mapMarkerManager.getHydrants())
                        fragment.areHydrantsSwitched = true
                    }
                    fragment.mapDecorator.addHydrants(fragment.mapMarkerManager.getHydrants(), location.longitude, location.latitude)
                }
                fragment.mapDecorator.updateDecoration(location)
                fragment.presenter.updatePosition(point(location.longitude, location.latitude), location.bearing.toDouble(), location.speed.toDouble())
            }

            if (fragment.autopilotPublisher.value!!) {
                fragment.updateMapCamera()
            } else {
                fragment.mapboxMap.locationComponent.forceLocationUpdate(location)
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can not be captured
         *
         * @param exception the exception message
         */
        override fun onFailure(@NonNull exception: Exception) {
            Timber.d(exception.localizedMessage)
            val fragment = fragmentWeakReference.get()
            if (fragment != null) {
                Toast.makeText(fragment.activity, exception.localizedMessage,
                        Toast.LENGTH_SHORT).show()
            }
        }
    }
}
