package hr.fer.help193.vehicle.core.connection

import com.tinder.scarlet.websocket.WebSocketEvent
import hr.fer.help193.vehicle.core.extension.logErrorAndRetry
import hr.fer.help193.vehicle.core.extension.shareReplayLatest
import hr.fer.help193.vehicle.core.extension.subscribeEmpty
import hr.fer.help193.vehicle.data.datasource.connection.WebSocketConnectionDatasource
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import timber.log.Timber

class ConnectionManagerImpl(private val webSocketConnectionDatasource: WebSocketConnectionDatasource,
                            private val backgroundScheduler: Scheduler) : ConnectionManager {

    private val serverReadyPublisher: BehaviorProcessor<Boolean> = BehaviorProcessor.createDefault(false)
    private val isServerReadyFlowable = serverReadyPublisher
            .shareReplayLatest()
            .observeOn(backgroundScheduler)

    private val disposables = CompositeDisposable()
    private var isServerReady: Boolean = false

    init {
        observeSignalsFromServer()
    }

    private fun observeSignalsFromServer() {
        disposables.add(webSocketConnectionDatasource.observeWebSocketConnection()
                .observeOn(backgroundScheduler)
                .distinctUntilChanged()
                .doOnNext(this::onServerReadyChange)
                .logErrorAndRetry()
                .subscribeEmpty()
        )
    }

    private fun onServerReadyChange(webSocketEvent: WebSocketEvent) {
        Timber.i("Dispatching web socket connection event > $webSocketEvent")
        when (webSocketEvent) {
            is WebSocketEvent.OnConnectionClosed -> connectionIsClosing()
            is WebSocketEvent.OnConnectionClosing -> connectionIsClosing()
            is WebSocketEvent.OnConnectionFailed -> connectionIsClosing()
            is WebSocketEvent.OnConnectionOpened -> connectionIsOpening()
        }
    }

    private fun connectionIsOpening() {
        this.isServerReady = true
        serverReadyPublisher.onNext(isServerReady)
    }

    private fun connectionIsClosing() {
        this.isServerReady = false
        serverReadyPublisher.onNext(isServerReady)
    }

    override fun isServerReady(): Flowable<Boolean> = isServerReadyFlowable
}
