package hr.fer.help193.vehicle.core.vehicletracker

import hr.fer.help193.share.model.rest.Vehicle
import hr.fer.help193.vehicle.core.extension.shareReplayLatest
import hr.fer.help193.vehicle.utils.isNotNull
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.processors.PublishProcessor

class VehicleTrackerImpl(backgroundScheduler: Scheduler) : VehicleTracker {

    private val vehiclePublisher: PublishProcessor<Vehicle?> = PublishProcessor.create()
    private val vehicle = vehiclePublisher
            .observeOn(backgroundScheduler)
            .filter(Vehicle::isNotNull)
            .distinctUntilChanged()
            .shareReplayLatest()

    override fun vehicle(): Flowable<Vehicle?> = vehicle

    override fun publishNewVehicle(vehicle: Vehicle?) {
        vehiclePublisher.onNext(vehicle)
    }
}
