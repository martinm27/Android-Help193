package hr.fer.help193.vehicle.app.base

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import hr.fer.help193.vehicle.core.navigation.router.main.MainRouter
import hr.fer.help193.vehicle.core.navigation.router.RoutingActionsDispatcher
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.FlowableProcessor
import timber.log.Timber

abstract class BasePresenter<in View, ViewState : Any> : ViewPresenter<View, ViewState> {

    lateinit var mainThreadScheduler: Scheduler
    lateinit var backgroundScheduler: Scheduler

    lateinit var routingActionsDispatcher: RoutingActionsDispatcher<MainRouter>

    private lateinit var viewState: ViewState
    private val viewStateProcessor: FlowableProcessor<ViewState> = BehaviorProcessor.create<ViewState>().toSerialized()

    private val disposables: CompositeDisposable = CompositeDisposable()
    private var viewObservingDisposable: Disposable = Disposables.disposed()

    /**
     * Avoid expensive allocation because this method is run on the Main Thread
     *
     * @return Initial view state to be rendered
     */
    @MainThread
    protected abstract fun initialViewState(): ViewState

    /**
     * Called only once when a presenter is created, after dependency injection.
     *
     * Override this method to implement initial presenter setup
     */
    final override fun start() {
        pushInitialViewState()
        onStart()
    }

    private fun pushInitialViewState() {
        viewState = initialViewState()
        viewStateProcessor.onNext(viewState)
    }

    /**
     * Called only once when a presenter is created, after dependency injection and initial view state is set.
     *
     * Override this method to implement initial presenter setup and queries
     */
    protected open fun onStart() {
        // Template
    }

    final override fun viewAttached(view: View) {
        if (!viewObservingDisposable.isDisposed) {
            throw IllegalStateException("Another's view disposable is not disposed")
        }

        viewObservingDisposable = observeView(view)
    }

    /**
     * Override to observe view.
     * DO NOT keep a direct reference to the passed view
     *
     * @return Disposable to be disposed when the view is gone
     */
    protected open fun observeView(view: View): Disposable = Disposables.disposed()

    final override fun viewState(): Flowable<ViewState> =
            viewStateProcessor.observeOn(mainThreadScheduler)
                    .subscribeOn(backgroundScheduler)

    final override fun viewDetached() {
        viewObservingDisposable.dispose()
        viewObservingDisposable = Disposables.disposed()
    }

    /**
     * Called only once, when a presenter is about to be destroyed
     */
    final override fun destroy() {
        onDestroy()
        disposables.clear()
    }

    /**
     * Called only once, when a presenter is about to be destroyed.
     *
     * Override this method to clear resources used by the presenter.
     *
     * Internal disposables will be disposed after this method completes.
     */
    protected open fun onDestroy() {
        // Template
    }

    /**
     * Invoke to route to another screen
     *
     * @param routingAction RoutingAction to dispatch
     */
    protected fun dispatchRoutingAction(routingAction: (MainRouter) -> Unit) = routingActionsDispatcher.dispatch(routingAction)

    protected fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    /**
     * Call this method to mutate current view state and to publish the new, mutated view state via [viewState] method.
     * This method should be called from a background thread since only rendering to the UI should be on the main thread.
     *
     * @param viewStateMutator to be called on the ViewState
     */
    @WorkerThread
    protected fun mutateViewState(viewStateMutator: (ViewState) -> Unit) {
        viewStateMutator(viewState)
        viewStateProcessor.onNext(viewState)
    }

    protected fun logError(throwable: Throwable) = Timber.w(throwable)

    protected fun runCommand(completable: Completable) {
        buildCommand(completable).assemble()
    }

    protected fun buildCommand(completable: Completable): QueryBuilder {
        return QueryBuilder(completable)
    }

    protected fun query(flowable: Flowable<(ViewState) -> Unit>) {
        buildQuery(flowable).assemble()
    }

    protected fun buildQuery(flowable: Flowable<(ViewState) -> Unit>) = QueryBuilder(flowable)

    inner class QueryBuilder {

        private val either: Either<Flowable<(ViewState) -> Unit>, Completable>

        constructor(viewStateMutator: Flowable<(ViewState) -> Unit>) {
            this.either = Either.Left(viewStateMutator)
        }

        constructor(command: Completable) {
            this.either = Either.Right(command)
        }

        fun assemble() {
            disposables.add(subscribe())
        }

        private fun subscribe() =
                either.fold({
                    it.subscribeOn(backgroundScheduler)
                            .subscribe(this@BasePresenter::mutateViewState, this@BasePresenter::logError)
                }, {
                    it.subscribeOn(backgroundScheduler)
                            .subscribe(Functions.EMPTY_ACTION, Consumer(this@BasePresenter::logError))
                })
    }
}
