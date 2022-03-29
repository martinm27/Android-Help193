package hr.fer.help193.vehicle.core.navigation.router

interface RoutingActionConsumer<T : Router> {

    fun onRoutingAction(routingAction: (T) -> Unit)
}
