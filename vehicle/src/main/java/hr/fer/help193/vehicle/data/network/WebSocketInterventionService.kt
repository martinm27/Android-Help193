package hr.fer.help193.vehicle.data.network

import com.tinder.scarlet.websocket.WebSocketEvent
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import hr.fer.help193.share.model.websocket.vehicle.MessageFromTablet.Assignment
import hr.fer.help193.share.model.websocket.vehicle.MessageFromTablet.PositionUpdate
import hr.fer.help193.share.model.websocket.vehicle.MessageToTablet.IncomingAssignment
import io.reactivex.Flowable

interface WebSocketInterventionService {

    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocketEvent>

    @Send
    fun sendPositionUpdate(positionUpdate: PositionUpdate)

    @Receive
    fun getIntervention(): Flowable<IncomingAssignment>

    @Send
    fun sendChosenIntervention(assignment: Assignment)
}
