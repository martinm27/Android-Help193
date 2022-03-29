package hr.fer.help193.vehicle.data.datasource.websocket

import hr.fer.help193.share.model.websocket.vehicle.MessageFromTablet
import hr.fer.help193.share.model.websocket.vehicle.MessageToTablet
import hr.fer.help193.vehicle.core.connection.ConnectionManager
import hr.fer.help193.vehicle.core.extension.resubscribeWhen
import hr.fer.help193.vehicle.core.extension.shareReplayLatest
import hr.fer.help193.vehicle.data.network.WebSocketInterventionService
import io.reactivex.Completable
import io.reactivex.Flowable
import timber.log.Timber

class WebSocketDatasourceImpl(private val webSocketInterventionService: WebSocketInterventionService,
                              private val connectionManager: ConnectionManager) : WebSocketDatasource {

    override fun sendChosenIntervention(assignment: MessageFromTablet.Assignment): Completable =
            Completable.fromAction { webSocketInterventionService.sendChosenIntervention(assignment) }.doOnComplete { Timber.i("Sending assignment event > $assignment") }

    override fun subscribeToIncomingAssignments(): Flowable<MessageToTablet.IncomingAssignment> =
            webSocketInterventionService.getIntervention()
                    .doOnNext { Timber.i("Dispatching web socket intervention: > $it") }
                    .resubscribeWhen(connectionManager.isServerReady())
                    .shareReplayLatest()

    override fun sendPositionUpdate(positionUpdate: MessageFromTablet.PositionUpdate): Completable =
            Completable.fromAction { webSocketInterventionService.sendPositionUpdate(positionUpdate) }
                    .doOnComplete { Timber.i("Sent position update event > $positionUpdate") }
                    .doOnError { Timber.w("Failed to update position: $positionUpdate with error: ${it.localizedMessage}") }
}
