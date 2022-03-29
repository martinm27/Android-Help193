package hr.fer.help193.vehicle.data.datasource.connection

import com.tinder.scarlet.websocket.WebSocketEvent
import io.reactivex.Flowable

interface WebSocketConnectionDatasource {

    fun observeWebSocketConnection(): Flowable<WebSocketEvent>
}
