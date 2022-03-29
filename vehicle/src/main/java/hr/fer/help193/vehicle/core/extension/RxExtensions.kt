package hr.fer.help193.vehicle.core.extension

import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.functions.Functions
import timber.log.Timber

fun <T> Flowable<T>.shareReplayLatest(): Flowable<T> = replay(1).refCount()

fun <T> Flowable<T>.logErrorAndRetry(): Flowable<T> = retry { throwable ->
    Timber.e(throwable)
    true
}

fun <T> Flowable<T>.subscribeEmpty(): Disposable = subscribe(Functions.emptyConsumer<Any>())

/**
 * Switch-maps to the source when [signal] emmits true.
 * Switch-maps to Flowable.never() while [signal] emmits false.
 */
fun <T> Flowable<T>.resubscribeWhen(signal: Flowable<Boolean>): Flowable<T> =
        signal.switchMap { if (it) this else Flowable.never() }
