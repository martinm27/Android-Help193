package hr.fer.help193.vehicle.core.navigation

import hr.fer.help193.vehicle.core.navigation.router.RoutingActionsDispatcher
import hr.fer.help193.vehicle.core.navigation.router.RoutingActionsSource
import hr.fer.help193.vehicle.core.navigation.router.RoutingMediator
import hr.fer.help193.vehicle.core.navigation.router.RoutingMediatorImpl
import hr.fer.help193.vehicle.core.navigation.router.main.MainRouter
import hr.fer.help193.vehicle.core.navigation.router.onboarding.OnboardingRouter
import org.koin.core.qualifier.named
import org.koin.dsl.module

val NavigationModule = module {

    single(named(MAIN_ROUTER_MEDIATOR)) { RoutingMediatorImpl<MainRouter>() as RoutingMediator<MainRouter> }
    single(named(MAIN_ROUTER_DISPATCHER)) { get<RoutingMediator<MainRouter>>(named(MAIN_ROUTER_MEDIATOR)) as RoutingActionsDispatcher<MainRouter> }
    single(named(MAIN_ROUTER_SOURCE)) { get<RoutingMediator<MainRouter>>(named(MAIN_ROUTER_MEDIATOR)) as RoutingActionsSource<MainRouter> }

    single(named(ONBOARDING_ROUTER_MEDIATOR)) { RoutingMediatorImpl<OnboardingRouter>() as RoutingMediator<OnboardingRouter> }
    single(named(ONBOARDING_ROUTER_DISPATCHER)) { get<RoutingMediator<OnboardingRouter>>(named(ONBOARDING_ROUTER_MEDIATOR)) as RoutingActionsDispatcher<OnboardingRouter> }
    single(named(ONBOARDING_ROUTER_SOURCE)) { get<RoutingMediator<OnboardingRouter>>(named(ONBOARDING_ROUTER_MEDIATOR)) as RoutingActionsSource<OnboardingRouter> }
}

const val MAIN_ROUTER_MEDIATOR = "Main router mediator"
const val MAIN_ROUTER_DISPATCHER = "Main router dispatcher"
const val MAIN_ROUTER_SOURCE = "Main router source"
const val ONBOARDING_ROUTER_MEDIATOR = "Onboarding router mediator"
const val ONBOARDING_ROUTER_DISPATCHER = "Onboarding router dispatcher"
const val ONBOARDING_ROUTER_SOURCE = "Onboarding router source"
