package hr.fer.help193.vehicle.app.features.map.usecase

import hr.fer.help193.share.model.rest.Tablet
import hr.fer.help193.vehicle.core.usecase.QueryUseCaseWithParam
import hr.fer.help193.vehicle.data.datasource.restapi.RestApiDatasource
import io.reactivex.Flowable

class QueryTabletData(private val restApiDatasource: RestApiDatasource) : QueryUseCaseWithParam<Int, Tablet> {

    override fun invoke(param: Int): Flowable<Tablet> = restApiDatasource.tablets(param)
}
