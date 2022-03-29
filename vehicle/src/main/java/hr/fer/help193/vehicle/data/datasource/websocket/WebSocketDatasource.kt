package hr.fer.help193.vehicle.data.datasource.websocket

import hr.fer.help193.share.model.websocket.vehicle.MessageFromTablet
import hr.fer.help193.share.model.websocket.vehicle.MessageFromTablet.PositionUpdate
import hr.fer.help193.share.model.websocket.vehicle.MessageToTablet.IncomingAssignment
import io.reactivex.Completable
import io.reactivex.Flowable

interface WebSocketDatasource {

    fun subscribeToIncomingAssignments(): Flowable<IncomingAssignment>

    fun sendPositionUpdate(positionUpdate: PositionUpdate): Completable

    fun sendChosenIntervention(assignment: MessageFromTablet.Assignment): Completable

}
