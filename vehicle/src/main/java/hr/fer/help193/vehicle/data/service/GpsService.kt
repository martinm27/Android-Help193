package hr.fer.help193.vehicle.data.service

import hr.fer.help193.share.model.websocket.vehicle.MessageFromTablet
import io.reactivex.Completable

interface GpsService {

    fun updatePosition(request: MessageFromTablet.PositionUpdate): Completable

}
