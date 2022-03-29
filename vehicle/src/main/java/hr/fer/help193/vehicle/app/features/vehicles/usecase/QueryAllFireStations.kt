package hr.fer.help193.vehicle.app.features.vehicles.usecase

import hr.fer.help193.share.model.rest.FireStation
import hr.fer.help193.vehicle.core.usecase.QueryUseCase
import hr.fer.help193.vehicle.data.datasource.restapi.RestApiDatasource
import io.reactivex.Flowable

class QueryAllFireStations(private val restApiDatasource: RestApiDatasource) : QueryUseCase<List<FireStation>> {

    override fun invoke(): Flowable<List<FireStation>> = restApiDatasource.firestations()
}

