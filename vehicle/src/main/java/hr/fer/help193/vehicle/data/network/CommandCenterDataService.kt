package hr.fer.help193.vehicle.data.network

import hr.fer.help193.share.model.rest.*
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CommandCenterDataService {

    @GET("api/fire-stations")
    fun getFirestations(): Flowable<List<FireStation>>

    @GET("api/hydrants")
    fun getHydrants(@Query("x") x: Double, @Query("y") y: Double, @Query("distance") distance: Double): Flowable<List<Hydrant>>

    @GET("api/interventions")
    fun getInterventions(): Flowable<List<Intervention>>

    @GET("api/vehicles")
    fun getVehicles(): Flowable<List<Vehicle>>

    @GET("api/documented-areas/{component1}/fire-stations/{component2}")
    fun getRouteDescriptions(@Path("component1") documentedAreaId: Int, @Path("component2") fireStationId: Int): Flowable<RouteDescription>

    @GET("api/tablets/{id}")
    fun getTabletData(@Path("id") id: Int): Flowable<Tablet>
}
