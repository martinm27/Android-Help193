package hr.fer.help193.vehicle.app.features.permissionerror.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.base.BaseFragment
import hr.fer.help193.vehicle.app.features.permissionerror.di.PERMISSION_ERROR_SCOPE
import kotlinx.android.synthetic.main.fragment_location_error.*

class PermissionErrorFragment : BaseFragment<PermissionErrorViewState>(), PermissionErrorContract.View {

    companion object {
        const val LAYOUT = R.layout.fragment_location_error
        const val TAG: String = "PermissionFragment"

        fun newInstance(): Fragment = PermissionErrorFragment()
    }

    private val presenter: PermissionErrorContract.Presenter by scopedInject()

    override fun getLayoutResource(): Int = LAYOUT
    override fun getScopeName(): String = PERMISSION_ERROR_SCOPE
    override fun getViewPresenter() = presenter

    override fun initialiseView(view: View, savedInstanceState: Bundle?) {
        permissionErrorTryAgain.setOnClickListener {
            presenter.requestPermission()
        }
    }

    override fun render(viewState: PermissionErrorViewState) {
    }
}
