package hr.fer.help193.vehicle.data.service

import hr.fer.help193.share.model.websocket.vehicle.MessageFromTablet
import hr.fer.help193.vehicle.core.connection.ConnectionManager
import hr.fer.help193.vehicle.core.extension.logErrorAndRetry
import hr.fer.help193.vehicle.core.extension.subscribeEmpty
import hr.fer.help193.vehicle.data.datasource.websocket.WebSocketDatasource
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.*

class GpsServiceImpl(connectionManager: ConnectionManager,
                     private val webSocketDatasource: WebSocketDatasource) : GpsService {

    private val updatesQueue = LinkedList<MessageFromTablet.PositionUpdate>()
    private var isServerReady: Boolean = false
    private val disposables = CompositeDisposable()

    init {
        observeServerReady(connectionManager)
    }

    private fun observeServerReady(connectionManager: ConnectionManager) {
        disposables.add(connectionManager.isServerReady()
                .distinctUntilChanged()
                .doOnNext(this::onServerReadyChange)
                .logErrorAndRetry()
                .subscribeEmpty())
    }

    @Synchronized
    private fun onServerReadyChange(isServerReady: Boolean) {
        this.isServerReady = isServerReady

        if (isServerReady) {
            flushQueue()
        }
    }

    private fun flushQueue() {
        while (updatesQueue.peek() != null && isServerReady) {
            webSocketDatasource.sendPositionUpdate(updatesQueue.poll())
        }
        updatesQueue.clear()
    }


    override fun updatePosition(request: MessageFromTablet.PositionUpdate): Completable {
        return if (isServerReady) {
            webSocketDatasource.sendPositionUpdate(request)
        } else {
            updatesQueue.add(request)
            Timber.w("Request queue because of connection issues > $request")
            Completable.complete()
        }
    }
}
