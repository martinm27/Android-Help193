package hr.fer.help193.vehicle.app.features.popup

import java.io.Serializable

data class PopupMessage(val title: String, val leftButton: PopupMessageButton, val rightButton: PopupMessageButton? = null) : Serializable

sealed class PopupMessageButton(open val title: String, open val action: () -> Unit) {

    data class RegularPopupMessageButton(override val title: String, override val action: () -> Unit) : PopupMessageButton(title, action)

    data class BoldPopupMessageButton(override val title: String, override val action: () -> Unit) : PopupMessageButton(title, action)
}
