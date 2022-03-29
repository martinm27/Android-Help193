package hr.fer.help193.vehicle.app.features.popup.ui

import hr.fer.help193.vehicle.app.base.BasePresenter
import hr.fer.help193.vehicle.app.features.popup.PopupMessage

class PopupMessagePresenter(private val popupMessage: PopupMessage) : BasePresenter<PopupMessageContract.View, PopupMessageViewState>(), PopupMessageContract.Presenter {

    override fun initialViewState() = PopupMessageViewState(popupMessage.title, popupMessage.leftButton, popupMessage.rightButton)

    override fun executeAction(action: () -> Unit) = action()
}
