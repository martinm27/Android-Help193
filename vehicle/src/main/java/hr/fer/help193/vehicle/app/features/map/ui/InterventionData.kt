package hr.fer.help193.vehicle.app.features.map.ui

import hr.fer.help193.share.model.rest.Hydrant
import hr.fer.help193.share.model.rest.Intervention
import hr.fer.help193.share.model.rest.RouteDescription

data class InterventionData(
        val intervention: Intervention,
        val hydrants: List<Hydrant>,
        val routeDescription: RouteDescription?
)
