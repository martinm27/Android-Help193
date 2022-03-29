package hr.fer.help193.vehicle.app.features.connection.ui

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.base.BaseFragment
import hr.fer.help193.vehicle.app.features.connection.di.CONNECTIVITY_BAR_SCOPE
import hr.fer.help193.vehicle.app.features.connection.ui.ConnectivityStatusViewModel.ConnectedStatusViewModel
import hr.fer.help193.vehicle.app.features.connection.ui.ConnectivityStatusViewModel.DisconnectedStatusViewModel
import hr.fer.help193.vehicle.utils.collapseViewHeight
import hr.fer.help193.vehicle.utils.expandViewHeight
import hr.fer.help193.vehicle.utils.removeDrawables
import hr.fer.help193.vehicle.utils.setLeftDrawable
import kotlinx.android.synthetic.main.fragment_connectivity_bar.*

class ConnectivityBarFragment : BaseFragment<ConnectivityBarViewState>(), ConnectivityBarContract.View {

    companion object {
        @LayoutRes
        val LAYOUT_RESOURCE = R.layout.fragment_connectivity_bar
    }

    private val presenter: ConnectivityBarContract.Presenter by scopedInject()

    override fun getLayoutResource() = LAYOUT_RESOURCE
    override fun getScopeName() = CONNECTIVITY_BAR_SCOPE
    override fun getViewPresenter() = presenter

    override fun render(viewState: ConnectivityBarViewState) {
        when (val viewModel = viewState.viewModel) {
            is ConnectedStatusViewModel -> renderConnectedStatus(viewModel)
            is DisconnectedStatusViewModel -> renderDisconnectedStatus(viewModel)
            ConnectivityStatusViewModel.EmptyConnectivityStatusViewModel -> view?.collapseViewHeight()
            else -> throw IllegalArgumentException("$viewModel is not supported")
        }
    }

    private fun renderConnectedStatus(viewModel: ConnectedStatusViewModel) {
        connectivityBar_title.setText(viewModel.connectivityStatus)
        (connectivityBar_title.currentView as TextView).setLeftDrawable(viewModel.drawableStart)
        animateBackgroundChange(ContextCompat.getColor(context!!, viewModel.backgroundColor))
    }

    private fun renderDisconnectedStatus(viewModel: DisconnectedStatusViewModel) {
        connectivityBar_title.setCurrentText(viewModel.connectivityStatus)
        (connectivityBar_title.currentView as TextView).removeDrawables()

        view?.run {
            setBackgroundColor(ContextCompat.getColor(context!!, viewModel.backgroundColor))
            expandViewHeight()
        }
    }

    private fun animateBackgroundChange(newBackgroundColor: Int) {
        view?.background?.let {
            ValueAnimator.ofObject(ArgbEvaluator(), (it as ColorDrawable).color, newBackgroundColor).apply {
                addUpdateListener { animator ->
                    view?.setBackgroundColor(animator.animatedValue as Int)
                }
                start()
            }
        } ?: view?.setBackgroundColor(newBackgroundColor)
    }
}
