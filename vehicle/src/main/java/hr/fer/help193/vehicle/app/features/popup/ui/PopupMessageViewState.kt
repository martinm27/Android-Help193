package hr.fer.help193.vehicle.app.features.popup.ui

import hr.fer.help193.vehicle.app.features.popup.PopupMessageButton

data class PopupMessageViewState(
        var title: String,
        var leftButton: PopupMessageButton,
        var rightButton: PopupMessageButton?
)
