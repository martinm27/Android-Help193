package hr.fer.help193.vehicle.app.features.map.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager
import hr.fer.help193.share.model.rest.Hydrant
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.core.ZAGREB_LATITUDE
import hr.fer.help193.vehicle.core.ZAGREB_LONGITUDE
import hr.fer.help193.vehicle.utils.isNotNull

class MapMarkerManager(private val markerViewManager: MarkerViewManager, private val context: Context) {

    private lateinit var vehicleMarker: MarkerView
    private lateinit var customVehicleView: View
    private lateinit var vehicleMarkerText: TextView

    private var interventionMarker: MarkerView? = null
    private lateinit var customInterventionView: View
    private lateinit var interventionIcon: ImageView

    private var hydrantMarkers: MutableList<MarkerView> = mutableListOf()
    private var hydrants: List<Hydrant> = emptyList()

    fun init() {
        customVehicleView = LayoutInflater.from(context).inflate(R.layout.item_map_marker, null)
        customVehicleView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        vehicleMarkerText = customVehicleView.findViewById(R.id.map_marker_title)

        customInterventionView = LayoutInflater.from(context).inflate(R.layout.item_fire_marker, null)
        customInterventionView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        interventionIcon = customInterventionView.findViewById(R.id.fire_marker)

        vehicleMarker = MarkerView(LatLng(ZAGREB_LATITUDE, ZAGREB_LONGITUDE), customVehicleView)
        interventionMarker = MarkerView(LatLng(ZAGREB_LATITUDE, ZAGREB_LONGITUDE), customInterventionView)

        markerViewManager.addMarker(vehicleMarker)
        markerViewManager.addMarker(interventionMarker!!)
    }

    fun updateVehicleDecoration(vehicleName: String, vehicleNameColor: Int) {
        vehicleMarkerText.text = vehicleName
        vehicleMarkerText.setTextColor(vehicleNameColor)
    }

    fun updateHydrants(mapHydrants: List<Hydrant>) {
        if (!hydrants.containsAll(mapHydrants)) {
            removeAllHydrants()

            if (mapHydrants.isNotEmpty()) {
                for (hydrant in mapHydrants) {
                    val hydrantView = LayoutInflater.from(context).inflate(R.layout.item_hydrant_marker, null)
                    hydrantView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    val hydrantName = hydrantView.findViewById<TextView>(R.id.hydrant_name)
                    val hydrantMarker = hydrantView.findViewById<ImageView>(R.id.hydrant_marker)

                    hydrantName.text = hydrant.inventoryMark
                    hydrantMarker.rotation = if (hydrant.placementId == 3) 180f else 0f
                    if (hydrant.isFunctional == null || !hydrant.isFunctional!!) hydrantMarker.setColorFilter(ContextCompat.getColor(context, R.color.red))

                    val hydrantMarkerView = MarkerView(LatLng(hydrant.location.y, hydrant.location.x), hydrantView)
                    hydrantMarkers.add(hydrantMarkerView)
                    markerViewManager.addMarker(hydrantMarkerView)
                }
            } else {
                Toast.makeText(context, String.format("Hidranti nedostupni."),
                        Toast.LENGTH_LONG).show()
            }
            hydrants = mapHydrants
        }
    }

    private fun removeAllHydrants() {
        if (hydrantMarkers.isNotEmpty()) {
            for (hydrant in hydrantMarkers) {
                markerViewManager.removeMarker(hydrant)
            }
        }
        hydrantMarkers.clear()
    }

    private fun updateMarker(latitude: Double, longitude: Double, markerView: MarkerView) {
        markerViewManager.removeMarker(markerView)
        markerView.setLatLng(LatLng(latitude, longitude))
        markerViewManager.addMarker(markerView)
    }

    fun updateIntervention(latitude: Double, longitude: Double) {
        if (interventionMarker.isNotNull()) {
            updateMarker(latitude, longitude, interventionMarker!!)
        }
    }

    fun updateVehicle(latitude: Double, longitude: Double) {
        updateMarker(latitude, longitude, vehicleMarker)
    }

    fun onDestroy() {
        markerViewManager.onDestroy()
    }

    fun getHydrants(): List<Hydrant> = hydrants

    fun clearHydrants() {
        removeAllHydrants()
        hydrants = emptyList()
    }
}
