package hr.fer.help193.vehicle.core.navigation.router.onboarding

import android.app.Activity
import android.content.Intent
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.activity.main.MainActivity
import hr.fer.help193.vehicle.app.features.onboarding.ui.OnboardingFragment
import hr.fer.help193.vehicle.utils.applyFadeInEnterAndFadeOutExitAnimation
import hr.fer.help193.vehicle.utils.inTransactionAndAddToBackStack

@IdRes
private const val ONBOARDING_CONTAINER_ID = R.id.onboarding_fragmentContainer

class OnboardingRouterImpl(private val activity: Activity,
                           private val fragmentManager: FragmentManager) : OnboardingRouter {

    override fun showOnboarding() {
        fragmentManager.inTransactionAndAddToBackStack {
            applyFadeInEnterAndFadeOutExitAnimation()
            add(ONBOARDING_CONTAINER_ID, OnboardingFragment.newInstance(), OnboardingFragment.TAG)
        }
    }

    override fun startMain() {
        val returnIntent = Intent(activity, MainActivity::class.java)
        activity.startActivity(returnIntent)
        activity.finish()
    }
}
