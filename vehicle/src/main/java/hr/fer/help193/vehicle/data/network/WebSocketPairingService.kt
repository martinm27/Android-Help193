package hr.fer.help193.vehicle.data.network

import com.tinder.scarlet.websocket.WebSocketEvent
import com.tinder.scarlet.ws.Receive
import hr.fer.help193.share.model.websocket.registration.RegistrationMessage
import io.reactivex.Flowable

interface WebSocketPairingService {

    @Receive
    fun observeRegistrationMessage(): Flowable<RegistrationMessage.PinMessage>

    @Receive
    fun observeSuccessMessage(): Flowable<RegistrationMessage.SuccessMessage>

    @Receive
    fun observeConnectionEvents(): Flowable<WebSocketEvent>
}
