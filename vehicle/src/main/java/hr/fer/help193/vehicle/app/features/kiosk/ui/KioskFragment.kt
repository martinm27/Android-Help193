package hr.fer.help193.vehicle.app.features.kiosk.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.base.BaseFragment
import hr.fer.help193.vehicle.app.features.kiosk.di.KIOSK_SCOPE
import kotlinx.android.synthetic.main.fragment_kiosk.*

class KioskFragment : BaseFragment<KioskViewState>(), KioskContract.View {

    companion object {
        const val LAYOUT = R.layout.fragment_kiosk
        const val TAG: String = "KioskFragment"

        fun newInstance(): Fragment = KioskFragment()
    }

    private val presenter: KioskContract.Presenter by scopedInject()

    override fun getLayoutResource(): Int = LAYOUT
    override fun getScopeName(): String = KIOSK_SCOPE
    override fun getViewPresenter() = presenter

    override fun initialiseView(view: View, savedInstanceState: Bundle?) {
        navigation_card.setOnClickListener { presenter.startNavigation() }
        exit_button.setOnLongClickListener {
            presenter.leaveApp()
            true
        }
    }

    override fun render(viewState: KioskViewState) {
    }
}
