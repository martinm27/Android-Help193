package hr.fer.help193.vehicle.core.interventiontracker

import hr.fer.help193.share.model.rest.Intervention
import hr.fer.help193.vehicle.core.extension.shareReplayLatest
import hr.fer.help193.vehicle.utils.isNotNull
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.processors.PublishProcessor

class InterventionTrackerImpl(backgroundScheduler: Scheduler) : InterventionTracker {

    private val interventionPublisher: PublishProcessor<Intervention?> = PublishProcessor.create()
    private val interventions = interventionPublisher
            .filter(Intervention::isNotNull)
            .observeOn(backgroundScheduler)
            .distinctUntilChanged()
            .shareReplayLatest()

    override fun intervention(): Flowable<Intervention?> = interventions

    override fun publishNewIntervention(intervention: Intervention?) {
        interventionPublisher.onNext(intervention)
    }
}
