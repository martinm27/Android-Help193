package hr.fer.help193.vehicle.app.features.map.map

import android.content.Context
import android.location.Location
import androidx.core.content.ContextCompat
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.style.sources.Source
import com.mapbox.turf.TurfConstants.UNIT_METERS
import com.mapbox.turf.TurfMeasurement
import hr.fer.help193.share.model.rest.Hydrant
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.core.MIN_ZOOM_LEVEL
import hr.fer.help193.vehicle.core.RESTRICTED_BOUNDS_AREA
import hr.fer.help193.vehicle.core.ZAGREB_LATITUDE
import hr.fer.help193.vehicle.core.ZAGREB_LONGITUDE

class MapDecorator(private val mapboxMap: MapboxMap, private val context: Context) {

    private var currentInterventionLatitude: Double = ZAGREB_LATITUDE
    private var currentInterventionLongitude: Double = ZAGREB_LONGITUDE

    fun init() {
        mapboxMap.setLatLngBoundsForCameraTarget(RESTRICTED_BOUNDS_AREA)
        // Set the minimum zoom level of the map camera
        mapboxMap.setMinZoomPreference(MIN_ZOOM_LEVEL)
    }

    fun setCurrentIntervention(latitude: Double, longitude: Double) {
        this.currentInterventionLatitude = latitude
        this.currentInterventionLongitude = longitude
    }

    fun updateDecoration(location: Location) {
        val points = listOf<Point>(
                Point.fromLngLat(location.longitude, location.latitude),
                Point.fromLngLat(currentInterventionLongitude, currentInterventionLatitude)
        )
        val lineString = LineString.fromLngLats(points)

        mapboxMap.style?.sources?.find { it.id == "line-source" }?.let {
            (it as GeoJsonSource).setGeoJson(Feature.fromGeometry(lineString))
        } ?: mapboxMap.style?.addSource(getGeoJsonSource(lineString))

        renderLine()
        renderSymbol(points)
    }

    private fun renderSymbol(points: List<Point>) {
        mapboxMap.style?.getLayer("symbol-source").let {
            if (it == null) {
                val symbolLayer = SymbolLayer("symbol-source", "line-source").apply {
                    withProperties(
                            PropertyFactory.textField(resolveDistance(TurfMeasurement.distance(points[0], points[1]))),
                            PropertyFactory.symbolPlacement(Property.SYMBOL_PLACEMENT_LINE_CENTER),
                            PropertyFactory.textAllowOverlap(true),
                            PropertyFactory.textKeepUpright(true),
                            PropertyFactory.textOffset(arrayOf(-2f, -2f))
                    )
                }
                mapboxMap.style?.addLayer(symbolLayer)
            } else {
                it.setProperties(PropertyFactory.textField(resolveDistance(TurfMeasurement.distance(points[0], points[1]))))
            }
        }
    }

    private fun resolveDistance(distance: Double): String =
            if (distance >= 1.0) {
                String.format("%.3f km", distance)
            } else {
                String.format("%.3f m", distance * 1000)
            }

    private fun renderLine() {
        val lineLayer = LineLayer("line-source", "line-source").apply {
            setProperties(
                    PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                    PropertyFactory.lineWidth(2f),
                    PropertyFactory.lineColor(ContextCompat.getColor(context, R.color.black))
            )
        }

        mapboxMap.style?.getLayer("line-source").let {
            if (it == null) {
                mapboxMap.style?.addLayer(lineLayer)
            }
        }
    }

    private fun getGeoJsonSource(lineString: LineString): Source =
            GeoJsonSource("line-source", FeatureCollection.fromFeatures(arrayOf<Feature>(Feature.fromGeometry(lineString))))

    fun addHydrants(hydrants: List<Hydrant>, longitude: Double, latitude: Double) {
        for (hydrant in hydrants) {
            val points = listOf<Point>(
                    Point.fromLngLat(hydrant.location.x, hydrant.location.y),
                    Point.fromLngLat(longitude, latitude)
            )
            val distanceInMeters = TurfMeasurement.distance(points[0], points[1], UNIT_METERS)

            if (distanceInMeters <= 100) {
                val lineString = LineString.fromLngLats(points)

                val sourceId = hydrant.inventoryMark ?: "Hydrant".plus(hydrant.location.x).plus(hydrant.location.y)

                mapboxMap.style?.sources?.find { it.id == sourceId }?.let {
                    (it as GeoJsonSource).setGeoJson(Feature.fromGeometry(lineString))
                    return
                }
                        ?: mapboxMap.style?.addSource(GeoJsonSource(sourceId, FeatureCollection.fromFeatures(arrayOf<Feature>(Feature.fromGeometry(lineString)))))

                val lineLayer = LineLayer("line-" + sourceId + "layer", sourceId).apply {
                    setProperties(
                            PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                            PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                            PropertyFactory.lineWidth(1f),
                            PropertyFactory.lineColor(ContextCompat.getColor(context, R.color.black))
                    )
                }

                mapboxMap.style?.getLayer("line-" + sourceId + "layer").let {
                    if (it == null) {
                        mapboxMap.style?.addLayer(lineLayer)
                    }
                }
                mapboxMap.style?.getLayer("symbol-" + sourceId + "layer").let {
                    if (it == null) {
                        val symbolLayer = SymbolLayer("symbol-" + sourceId + "layer", sourceId).apply {
                            withProperties(
                                    PropertyFactory.textField(resolveDistance(TurfMeasurement.distance(points[0], points[1]))),
                                    PropertyFactory.symbolPlacement(Property.SYMBOL_PLACEMENT_LINE_CENTER),
                                    PropertyFactory.textAllowOverlap(true),
                                    PropertyFactory.textKeepUpright(true),
                                    PropertyFactory.textOffset(arrayOf(-1f, -1f))
                            )
                        }
                        mapboxMap.style?.addLayer(symbolLayer)
                    } else {
                        it.setProperties(PropertyFactory.textField(resolveDistance(TurfMeasurement.distance(points[0], points[1]))))
                    }
                }
            }
        }
    }

    fun clearHydrants(hydrants: List<Hydrant>) {
        for (hydrant in hydrants) {
            val sourceId = hydrant.inventoryMark ?: "Hydrant".plus(hydrant.location.x).plus(hydrant.location.y)
            mapboxMap.style?.removeLayer("line-" + sourceId + "layer")
            mapboxMap.style?.removeLayer("symbol-" + sourceId + "layer")
            mapboxMap.style?.removeSource(sourceId)
        }
    }

    fun animateToPosition(latitude: Double, longitude: Double) {
        val bounds = LatLngBounds.Builder().include(LatLng(latitude, longitude)).include(LatLng(currentInterventionLatitude, currentInterventionLongitude)).build()
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    fun clear() {
        mapboxMap.style?.removeLayer("line-source")
        mapboxMap.style?.removeLayer("symbol-source")
        mapboxMap.style?.removeSource("line-source")
    }
}
