package hr.fer.help193.vehicle.app.features.connection.ui

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

sealed class ConnectivityStatusViewModel {
    object EmptyConnectivityStatusViewModel : ConnectivityStatusViewModel()
    data class DisconnectedStatusViewModel(val connectivityStatus: String, @ColorRes val backgroundColor: Int) : ConnectivityStatusViewModel()
    data class ConnectedStatusViewModel(val connectivityStatus: String, @ColorRes val backgroundColor: Int, @DrawableRes val drawableStart: Int) : ConnectivityStatusViewModel()
}
