package hr.fer.help193.vehicle.app.features.connection.resources

import android.content.res.Resources
import hr.fer.help193.vehicle.R

class ConnectivityStatusResourcesImpl(private val resources: Resources) : ConnectivityStatusResources {

    override fun connectedLabelText(): String = resources.getString(R.string.connectivitybar_connectedStatus)

    override fun disconnectedLabelText(): String = resources.getString(R.string.connectivitybar_disconnectedStatus)

    override fun connectedDrawableRes() = R.drawable.ic_check_white

    override fun connectedLabelBackgroundColorRes(): Int = R.color.green

    override fun disconnectedLabelBackgroundColorRes(): Int = R.color.red
}
