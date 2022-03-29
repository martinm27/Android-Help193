package hr.fer.help193.vehicle.data.datasource.connection

import com.tinder.scarlet.websocket.WebSocketEvent
import hr.fer.help193.vehicle.core.extension.shareReplayLatest
import hr.fer.help193.vehicle.data.network.WebSocketInterventionService
import io.reactivex.Flowable
import timber.log.Timber

class WebSocketConnectionDatasourceImpl(private val webSocketInterventionService: WebSocketInterventionService) : WebSocketConnectionDatasource {

    override fun observeWebSocketConnection(): Flowable<WebSocketEvent> =
            webSocketInterventionService.observeWebSocketEvent()
                    .filter { it !is WebSocketEvent.OnMessageReceived }
                    .doOnNext { Timber.i("Dispatching web socket connection: > $it") }
                    .distinctUntilChanged()
                    .shareReplayLatest()
}
