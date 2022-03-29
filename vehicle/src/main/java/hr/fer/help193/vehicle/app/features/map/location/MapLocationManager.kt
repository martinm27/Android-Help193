package hr.fer.help193.vehicle.app.features.map.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.Style
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.core.DEFAULT_INTERVAL_IN_MILLISECONDS
import hr.fer.help193.vehicle.core.DEFAULT_MAX_WAIT_TIME

class MapLocationManager(private val locationComponent: LocationComponent, private val context: Context, private val callback: LocationEngineCallback<LocationEngineResult>) {

    private lateinit var locationEngine: LocationEngine

    @SuppressLint("MissingPermission")
    fun enableLocationComponent(style: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(context)) {
            // Get an instance of the component
            val customLocationComponentOptions = LocationComponentOptions.builder(context)
                    .elevation(5f)
                    .bearingDrawable(R.drawable.ic_firefighter_drawable)
                    .build()
            // Set the LocationComponent activation options
            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(context, style)
                    .locationComponentOptions(customLocationComponentOptions)
                    .useDefaultLocationEngine(false)
                    .build()
            // Activate with the LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(locationComponentActivationOptions)

            // Enable to make component visible
            locationComponent.isLocationComponentEnabled = true

            // Set the component's camera mode
            locationComponent.cameraMode = CameraMode.TRACKING

            // Set the component's render mode
            locationComponent.renderMode = RenderMode.COMPASS

            initLocationEngine()
        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(context)

        val request = LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build()

        locationEngine.requestLocationUpdates(request, callback, Looper.getMainLooper())
        locationEngine.getLastLocation(callback)
    }

    fun removeLocationUpdates() {
        locationEngine.removeLocationUpdates(callback)
    }
}
