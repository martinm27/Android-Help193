package hr.fer.help193.vehicle.app.features.map.usecase

import hr.fer.help193.share.model.websocket.vehicle.MessageFromTablet
import hr.fer.help193.vehicle.core.interventiontracker.InterventionTracker
import hr.fer.help193.vehicle.core.usecase.CommandUseCaseWithParam
import hr.fer.help193.vehicle.data.service.GpsService
import io.reactivex.Completable
import org.joda.time.DateTime
import org.locationtech.jts.geom.Point

class UpdatePosition(private val interventionTracker: InterventionTracker,
                     private val gpsService: GpsService) : CommandUseCaseWithParam<UpdatePosition.Request> {

    override fun invoke(param: Request): Completable =
            interventionTracker.intervention()
                    .firstOrError()
                    .flatMapCompletable {
                        if (it.isActive) {
                            gpsService.updatePosition(MessageFromTablet.PositionUpdate(
                                    param.point,
                                    DateTime.now(),
                                    param.speed,
                                    param.heading
                            ))
                        } else {
                            Completable.complete()
                        }
                    }

    class Request(val point: Point, val speed: Double, val heading: Double)
}
