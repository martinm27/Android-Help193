package hr.fer.help193.vehicle.data.datasource.restapi

import hr.fer.help193.share.model.rest.*
import io.reactivex.Flowable

interface RestApiDatasource {

    fun firestations(): Flowable<List<FireStation>>

    fun hydrants(x: Double, y: Double, distance: Double): Flowable<List<Hydrant>>

    fun interventions(): Flowable<List<Intervention>>

    fun vehicles(): Flowable<List<Vehicle>>

    fun routeDescriptions(documentedAreaId: Int, firestationId: Int): Flowable<RouteDescription>

    fun tablets(tabletId: Int): Flowable<Tablet>
}
