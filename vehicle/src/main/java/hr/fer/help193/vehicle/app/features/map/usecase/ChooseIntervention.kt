package hr.fer.help193.vehicle.app.features.map.usecase

import hr.fer.help193.share.model.websocket.vehicle.MessageFromTablet
import hr.fer.help193.vehicle.core.usecase.CommandUseCaseWithParam
import hr.fer.help193.vehicle.data.datasource.websocket.WebSocketDatasource
import io.reactivex.Completable

class ChooseIntervention(private val webSocketDatasource: WebSocketDatasource) : CommandUseCaseWithParam<ChooseIntervention.Request> {

    override fun invoke(param: Request): Completable = webSocketDatasource.sendChosenIntervention(MessageFromTablet.Assignment(param.interventionId, param.vehicleId))

    class Request(val interventionId: Int, val vehicleId: Int)
}
