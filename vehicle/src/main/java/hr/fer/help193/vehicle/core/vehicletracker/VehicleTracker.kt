package hr.fer.help193.vehicle.core.vehicletracker

import hr.fer.help193.share.model.rest.Vehicle
import io.reactivex.Flowable

interface VehicleTracker {

    fun publishNewVehicle(vehicle: Vehicle?)

    fun vehicle(): Flowable<Vehicle?>
}
