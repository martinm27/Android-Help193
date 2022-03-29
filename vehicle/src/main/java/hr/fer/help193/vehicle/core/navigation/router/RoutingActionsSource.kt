package hr.fer.help193.vehicle.core.navigation.router

interface RoutingActionsSource<T : Router> {

    fun setActiveRoutingActionConsumer(routingActionConsumer: RoutingActionConsumer<T>)

    fun unsetRoutingActionConsumer(routingActionConsumer: RoutingActionConsumer<T>)
}
