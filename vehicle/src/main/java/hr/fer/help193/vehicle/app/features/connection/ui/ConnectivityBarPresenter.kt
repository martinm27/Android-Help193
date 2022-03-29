package hr.fer.help193.vehicle.app.features.connection.ui

import hr.fer.help193.vehicle.app.base.BasePresenter
import hr.fer.help193.vehicle.app.features.connection.resources.ConnectivityStatusResources
import hr.fer.help193.vehicle.app.features.connection.ui.ConnectivityBarStatus.*
import hr.fer.help193.vehicle.app.features.connection.ui.ConnectivityStatusViewModel.*
import hr.fer.help193.vehicle.app.features.connection.usecase.QueryIsServerReadyWithDelay
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

private const val CONNECTED_STATUS_SHOW_DURATION = 5000L

class ConnectivityBarPresenter(private val connectivityStatusResources: ConnectivityStatusResources,
                               private val queryIsServerReadyWithDelay: QueryIsServerReadyWithDelay)
    : BasePresenter<ConnectivityBarContract.View, ConnectivityBarViewState>(), ConnectivityBarContract.Presenter {

    private val connectedStatus: ConnectedStatusViewModel by lazy {
        ConnectedStatusViewModel(connectivityStatusResources.connectedLabelText(),
                connectivityStatusResources.connectedLabelBackgroundColorRes(),
                connectivityStatusResources.connectedDrawableRes())
    }

    private val disconnectedStatus by lazy {
        DisconnectedStatusViewModel(connectivityStatusResources.disconnectedLabelText(),
                connectivityStatusResources.disconnectedLabelBackgroundColorRes())
    }

    override fun initialViewState(): ConnectivityBarViewState = ConnectivityBarViewState(EmptyConnectivityStatusViewModel)

    override fun onStart() {
        query(queryIsServerReadyWithDelay()
                .scan(INITIAL)
                { currentConnectivityState, isServerReady ->
                    determineConnectivityStatus(currentConnectivityState, isServerReady)
                }
                .switchMap { if (it === CONNECTING) startTimer(it) else Flowable.just(it) }
                .map(this::toViewStateAction))
    }

    private fun determineConnectivityStatus(currentConnectionState: ConnectivityBarStatus, isPyngReady: Boolean): ConnectivityBarStatus =
            if (!isPyngReady) {
                DISCONNECTED
            } else {
                when (currentConnectionState) {
                    INITIAL -> INITIAL
                    DISCONNECTED -> CONNECTING
                    else -> CONNECTED
                }
            }

    private fun startTimer(connectingStatus: ConnectivityBarStatus): Flowable<ConnectivityBarStatus> =
            Flowable.timer(CONNECTED_STATUS_SHOW_DURATION, TimeUnit.MILLISECONDS, backgroundScheduler)
                    .map { CONNECTED }
                    .startWith(connectingStatus)

    private fun toViewStateAction(newConnectivityStatus: ConnectivityBarStatus) = { viewState: ConnectivityBarViewState ->
        viewState.viewModel =
                when (newConnectivityStatus) {
                    CONNECTING -> connectedStatus
                    DISCONNECTED -> disconnectedStatus
                    else -> EmptyConnectivityStatusViewModel
                }

    }
}

enum class ConnectivityBarStatus {
    INITIAL,
    CONNECTING,
    CONNECTED,
    DISCONNECTED
}
