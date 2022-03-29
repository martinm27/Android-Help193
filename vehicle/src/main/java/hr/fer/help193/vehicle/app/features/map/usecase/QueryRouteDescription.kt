package hr.fer.help193.vehicle.app.features.map.usecase

import hr.fer.help193.share.model.rest.RouteDescription
import hr.fer.help193.vehicle.core.usecase.QueryUseCaseWithParam
import hr.fer.help193.vehicle.data.datasource.restapi.RestApiDatasource
import io.reactivex.Flowable

class QueryRouteDescription(private val restApiDatasource: RestApiDatasource) : QueryUseCaseWithParam<QueryRouteDescription.Request, RouteDescription> {

    override fun invoke(param: Request): Flowable<RouteDescription> = restApiDatasource.routeDescriptions(param.documentedAreaId, param.firestationId)

    data class Request(val documentedAreaId: Int, val firestationId: Int)
}
