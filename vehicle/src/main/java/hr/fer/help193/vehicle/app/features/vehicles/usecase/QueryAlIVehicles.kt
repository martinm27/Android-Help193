package hr.fer.help193.vehicle.app.features.vehicles.usecase

import hr.fer.help193.share.model.rest.Vehicle
import hr.fer.help193.vehicle.core.usecase.QueryUseCase
import hr.fer.help193.vehicle.data.datasource.restapi.RestApiDatasource
import io.reactivex.Flowable

class QueryAllVehicles(private val restApiDatasource: RestApiDatasource) : QueryUseCase<List<Vehicle>> {

    override fun invoke(): Flowable<List<Vehicle>> = restApiDatasource.vehicles()
}
