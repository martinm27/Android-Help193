package hr.fer.help193.vehicle.core.connection

import io.reactivex.Flowable

interface ConnectionManager {

    fun isServerReady(): Flowable<Boolean>

}
