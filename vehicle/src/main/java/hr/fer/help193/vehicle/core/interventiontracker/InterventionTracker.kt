package hr.fer.help193.vehicle.core.interventiontracker

import hr.fer.help193.share.model.rest.Intervention
import io.reactivex.Flowable

interface InterventionTracker {

    fun publishNewIntervention(intervention: Intervention?)

    fun intervention(): Flowable<Intervention?>
}
