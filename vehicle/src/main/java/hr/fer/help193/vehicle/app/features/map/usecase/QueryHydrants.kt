package hr.fer.help193.vehicle.app.features.map.usecase

import hr.fer.help193.share.model.rest.Hydrant
import hr.fer.help193.vehicle.core.usecase.QueryUseCaseWithParam
import hr.fer.help193.vehicle.data.datasource.restapi.RestApiDatasource
import hr.fer.help193.vehicle.utils.isNotNull
import io.reactivex.Flowable

class QueryHydrants(private val restApiDatasource: RestApiDatasource) : QueryUseCaseWithParam<QueryHydrants.Request, List<Hydrant>> {

    override fun invoke(param: Request): Flowable<List<Hydrant>> {
        return restApiDatasource.hydrants(param.longitude, param.latitude, param.distance)
    }

    data class Request(val longitude: Double, val latitude: Double, val distance: Double)

}
