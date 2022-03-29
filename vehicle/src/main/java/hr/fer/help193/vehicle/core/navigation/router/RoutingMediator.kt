package hr.fer.help193.vehicle.core.navigation.router

interface RoutingMediator<T : Router> : RoutingActionsDispatcher<T>, RoutingActionsSource<T>
