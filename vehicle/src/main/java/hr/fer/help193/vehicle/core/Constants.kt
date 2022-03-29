package hr.fer.help193.vehicle.core

import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds

const val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
const val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5
const val ZAGREB_LATITUDE = 45.815399
const val ZAGREB_LONGITUDE = 15.966568
const val HYDRANTS_DISTANCE_PERIMETER = 0.004
const val LOCATION_DISTANCE_TOLERANCE = 0.0001
val BOUND_CORNER_NW = LatLng(45.620123, 15.779632)
val BOUND_CORNER_SE = LatLng(45.962736, 16.247934)
val RESTRICTED_BOUNDS_AREA: LatLngBounds = LatLngBounds.Builder().include(BOUND_CORNER_NW).include(BOUND_CORNER_SE).build()
const val MIN_ZOOM_LEVEL = 11.0
const val MAX_ZOOM_LEVEL = 15.5
