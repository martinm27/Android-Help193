package hr.fer.help193.vehicle.app.features.connection.resources

interface ConnectivityStatusResources {

    fun connectedLabelText() : String

    fun disconnectedLabelText() : String

    fun connectedDrawableRes() : Int

    fun connectedLabelBackgroundColorRes() : Int

    fun disconnectedLabelBackgroundColorRes() : Int
}
