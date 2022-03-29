package hr.fer.help193.vehicle.app.features.map.ui

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import hr.fer.help193.share.model.rest.Hydrant

data class MapViewState(
        var isInterventionActive: Boolean,
        var description: String,
        var vehicleName: String,
        var latitude: Double,
        var longitude: Double,
        var mapHydrants: List<Hydrant>,
        @DrawableRes var interventionIcon: Int,
        @ColorInt var vehicleNameColor: Int
)
