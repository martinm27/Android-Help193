package hr.fer.help193.vehicle.app.features.map.usecase

import hr.fer.help193.share.model.websocket.vehicle.MessageToTablet
import hr.fer.help193.vehicle.core.usecase.QueryUseCase
import hr.fer.help193.vehicle.data.datasource.websocket.WebSocketDatasource
import io.reactivex.Flowable

class QueryNewInterventions(private val webSocketDatasource: WebSocketDatasource) : QueryUseCase<MessageToTablet.IncomingAssignment> {

    override fun invoke(): Flowable<MessageToTablet.IncomingAssignment> =
            webSocketDatasource.subscribeToIncomingAssignments()
}
