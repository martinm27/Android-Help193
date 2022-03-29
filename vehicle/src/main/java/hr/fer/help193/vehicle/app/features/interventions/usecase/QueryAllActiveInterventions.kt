package hr.fer.help193.vehicle.app.features.interventions.usecase

import hr.fer.help193.share.model.rest.Intervention
import hr.fer.help193.vehicle.core.usecase.QueryUseCase
import hr.fer.help193.vehicle.data.datasource.restapi.RestApiDatasource
import hr.fer.help193.vehicle.utils.isNotNull
import io.reactivex.Flowable

class QueryAllActiveInterventions(private val restApiDatasource: RestApiDatasource) : QueryUseCase<List<Intervention>> {

    override fun invoke(): Flowable<List<Intervention>> =
            restApiDatasource.interventions()
                    .map { interventions -> interventions.filter { intervention -> intervention.id.isNotNull() && intervention.isActive } }
}
