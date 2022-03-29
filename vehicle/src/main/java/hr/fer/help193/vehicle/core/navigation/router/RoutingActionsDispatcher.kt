package hr.fer.help193.vehicle.core.navigation.router

interface RoutingActionsDispatcher<T : Router> {

    fun dispatch(routingAction: (T) -> Unit)
}
