package hr.fer.help193.vehicle.app.features.onboarding.di

import hr.fer.help193.vehicle.app.di.BACKGROUND_SCHEDULER
import hr.fer.help193.vehicle.app.di.MAIN_SCHEDULER
import hr.fer.help193.vehicle.app.features.onboarding.ui.OnboardingContract
import hr.fer.help193.vehicle.app.features.onboarding.ui.OnboardingPresenter
import hr.fer.help193.vehicle.core.navigation.MAIN_ROUTER_DISPATCHER
import org.koin.core.qualifier.named
import org.koin.dsl.module

val OnboardingModule = module {

    scope(named(ONBOARDING_SCOPE)) {

        scoped<OnboardingContract.Presenter> {
            OnboardingPresenter(get()).apply {
                mainThreadScheduler = get(named(MAIN_SCHEDULER))
                backgroundScheduler = get(named(BACKGROUND_SCHEDULER))
                routingActionsDispatcher = get(named(MAIN_ROUTER_DISPATCHER))
                start()
            }
        }
    }
}

const val ONBOARDING_SCOPE = "Onboarding Scope"
