package hr.fer.help193.vehicle.app.features.onboarding.ui

import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.activity.onboarding.OnboardingActivity
import hr.fer.help193.vehicle.app.base.BaseFragment
import hr.fer.help193.vehicle.app.features.onboarding.di.ONBOARDING_SCOPE
import hr.fer.help193.vehicle.utils.show
import kotlinx.android.synthetic.main.fragment_onboarding.*

class OnboardingFragment : BaseFragment<OnboardingViewState>(), OnboardingContract.View {

    companion object {
        const val LAYOUT = R.layout.fragment_onboarding
        const val TAG: String = "OnboardingFragment"

        fun newInstance(): Fragment = OnboardingFragment()
    }

    private val presenter: OnboardingContract.Presenter by scopedInject()

    override fun getLayoutResource(): Int = LAYOUT
    override fun getScopeName(): String = ONBOARDING_SCOPE
    override fun getViewPresenter() = presenter

    override fun render(viewState: OnboardingViewState) {
        with(viewState) {
            if (username != null && password != null && tabletId != null) {
                (activity as OnboardingActivity).saveCredentials(username!!, password!!, tabletId!!)
            }

            if (pin.isEmpty()) {
                onboarding_logo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce_animation))
            } else {
                onboarding_logo.clearAnimation()
                onboarding_logo.show(false)
                onboarding_image_gif.show(false)
                onboarding_pin_message.text = String.format(resources.getString(R.string.onboarding_pin_message), pin)
                onboarding_pin_message.show(true)
            }
        }
    }
}
