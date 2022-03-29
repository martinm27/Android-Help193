package hr.fer.help193.vehicle.utils

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.PrecisionModel

fun point(x: Double, y: Double) = GeometryFactory(PrecisionModel(), 4326).createPoint(Coordinate(x, y))!!
