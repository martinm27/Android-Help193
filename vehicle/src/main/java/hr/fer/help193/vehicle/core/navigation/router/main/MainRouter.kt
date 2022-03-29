package hr.fer.help193.vehicle.core.navigation.router.main

import hr.fer.help193.vehicle.app.features.popup.PopupMessage
import hr.fer.help193.vehicle.core.navigation.router.Router

interface MainRouter : Router {

    fun goBack()

    fun goBackThrottled()

    fun showErrorState()

    fun showMap()

    fun startNavigation()

    fun leaveApp()

    fun showKiosk()

    fun returnToKiosk()

    fun returnToMap()

    fun showPopupMessage(popupMessage: PopupMessage)

    fun closePopup(): Boolean

    fun showAllInterventions()

    fun showAllVehicles(value: Int)

    fun showInterventionDetails()
}
