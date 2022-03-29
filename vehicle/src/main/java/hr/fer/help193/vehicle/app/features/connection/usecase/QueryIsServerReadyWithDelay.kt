package hr.fer.help193.vehicle.app.features.connection.usecase

import hr.fer.help193.vehicle.core.connection.ConnectionManager
import hr.fer.help193.vehicle.core.extension.shareReplayLatest
import hr.fer.help193.vehicle.core.usecase.QueryUseCase
import io.reactivex.Flowable

/**
 * @return Boolean value that is passed depending on whether the server connection is active or not.
 */
class QueryIsServerReadyWithDelay(connectionManager: ConnectionManager) : QueryUseCase<Boolean> {

    private val connectivityBarStatus: Flowable<Boolean> = connectionManager.isServerReady()
            .distinctUntilChanged()
            .shareReplayLatest()

    override fun invoke(): Flowable<Boolean> = connectivityBarStatus
}
