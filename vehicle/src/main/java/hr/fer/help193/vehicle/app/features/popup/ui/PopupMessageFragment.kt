package hr.fer.help193.vehicle.app.features.popup.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.base.BaseFragment
import hr.fer.help193.vehicle.app.features.popup.PopupMessage
import hr.fer.help193.vehicle.app.features.popup.PopupMessageButton
import hr.fer.help193.vehicle.app.features.popup.di.POPUP_MESSAGE_VIEW_SCOPE
import hr.fer.help193.vehicle.utils.*
import kotlinx.android.synthetic.main.fragment_popup_message.*
import org.koin.core.parameter.parametersOf

private const val KEY_POPUP_MESSAGE = "key_popup_message"

class PopupMessageFragment : BaseFragment<PopupMessageViewState>(), PopupMessageContract.View {

    companion object {
        @LayoutRes
        val LAYOUT_RESOURCE: Int = R.layout.fragment_popup_message

        const val TAG = "PopupMessage"

        fun newInstance(popupMessage: PopupMessage): Fragment =
                PopupMessageFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(KEY_POPUP_MESSAGE, popupMessage)
                    }
                }
    }

    private val presenter: PopupMessageContract.Presenter by scopedInject(parameters = { parametersOf(arguments!!.getSerializableOrThrow(KEY_POPUP_MESSAGE)) })

    override fun getScopeName() = POPUP_MESSAGE_VIEW_SCOPE
    override fun getLayoutResource() = LAYOUT_RESOURCE
    override fun getViewPresenter() = presenter

    override fun initialiseView(view: View, savedInstanceState: Bundle?) {
        view.hideKeyboard()
    }

    override fun render(viewState: PopupMessageViewState) {
        with(viewState) {
            popupmessage_title.text = title

            popupmessage_leftButton.text = leftButton.title
            popupmessage_leftButton.setOnClickListener { presenter.executeAction { leftButton.action() } }
            setButtonTextStyle(popupmessage_leftButton, leftButton)

            rightButton?.let { button ->
                popupmessage_rightButton.text = button.title
                popupmessage_rightButton.setOnClickListener { presenter.executeAction { button.action() } }
                setButtonTextStyle(popupmessage_rightButton, button)
            }

            popupmessage_rightButtonGroup.show(rightButton.isNotNull())
        }
    }

    private fun setButtonTextStyle(textView: TextView, button: PopupMessageButton) {
        when (button) {
            is PopupMessageButton.RegularPopupMessageButton -> textView.setTextAppearanceCompat(R.style.TextB1)
            is PopupMessageButton.BoldPopupMessageButton -> textView.setTextAppearanceCompat(R.style.TextH3)
            else -> throw IllegalArgumentException("Unsupported popup message button type")
        }
    }
}
