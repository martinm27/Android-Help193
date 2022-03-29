package hr.fer.help193.vehicle.core.navigation.router

import timber.log.Timber
import java.util.*

class RoutingMediatorImpl<T : Router> : RoutingMediator<T> {

    private val QUEUE_LOCK = Any()
    private val routingActionQueue = LinkedList<(T) -> Unit>()

    private var routingActionConsumer: RoutingActionConsumer<T>? = null

    override fun dispatch(routingAction: (T) -> Unit) {
        synchronized(QUEUE_LOCK) {
            routingActionConsumer
                    ?.onRoutingAction(routingAction)
                    ?: routingActionQueue.add(routingAction)
        }
    }

    override fun setActiveRoutingActionConsumer(routingActionConsumer: RoutingActionConsumer<T>) {
        if (this.routingActionConsumer != null) {
            Timber.w("Setting action consumer before the previous one was unset")
        }

        synchronized(QUEUE_LOCK) {
            this.routingActionConsumer ?: flushRoutingActions(routingActionConsumer)
            this.routingActionConsumer = routingActionConsumer
        }
    }

    private fun flushRoutingActions(routingActionConsumer: RoutingActionConsumer<T>) {
        while (routingActionQueue.peek() != null) {
            routingActionConsumer.onRoutingAction(routingActionQueue.poll())
        }
    }

    override fun unsetRoutingActionConsumer(routingActionConsumer: RoutingActionConsumer<T>) {
        if (this.routingActionConsumer !== routingActionConsumer) {
            Timber.w("Can't unset foreign consumer")
            return
        }
        this.routingActionConsumer = null
    }
}
