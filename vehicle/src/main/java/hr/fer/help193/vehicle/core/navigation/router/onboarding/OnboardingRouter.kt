package hr.fer.help193.vehicle.core.navigation.router.onboarding

import hr.fer.help193.vehicle.core.navigation.router.Router

interface OnboardingRouter : Router {

    fun showOnboarding()

    fun startMain()
}
