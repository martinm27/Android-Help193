package hr.fer.help193.vehicle.app.features.onboarding.ui

import hr.fer.help193.share.model.websocket.registration.RegistrationMessage
import hr.fer.help193.vehicle.app.base.BasePresenter
import hr.fer.help193.vehicle.data.network.WebSocketPairingService
import hr.fer.help193.vehicle.core.navigation.router.main.MainRouter
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class OnboardingPresenter(private val webSocketPairingService: WebSocketPairingService)
    : BasePresenter<OnboardingContract.View, OnboardingViewState>(), OnboardingContract.Presenter {

    private lateinit var registrationMessageDisposable: Disposable

    override fun initialViewState(): OnboardingViewState = OnboardingViewState("", null, null, null)

    override fun onStart() {
        registrationMessageDisposable = webSocketPairingService.observeRegistrationMessage()
                .observeOn(backgroundScheduler)
                .subscribeOn(backgroundScheduler)
                .subscribeBy(onNext = {
                    showPinMessage(it)
                }, onError = {
                    logError(it)
                })

        query(webSocketPairingService.observeSuccessMessage()
                .map { successMesage ->
                    { viewState: OnboardingViewState ->
                        viewState.username = successMesage.username
                        viewState.password = successMesage.key
                        viewState.tabletId = successMesage.tabletId
                    }
                })

        addDisposable(webSocketPairingService.observeConnectionEvents()
                .observeOn(backgroundScheduler)
                .subscribeOn(backgroundScheduler)
                .subscribe { Timber.d(it.toString()) })
    }


    override fun startApp() = dispatchRoutingAction(MainRouter::startNavigation)

    private fun showPinMessage(pinMessage: RegistrationMessage.PinMessage?) {
        if (!registrationMessageDisposable.isDisposed) {
            registrationMessageDisposable.dispose()
        }

        mutateViewState {
            it.pin = pinMessage?.pin ?: ""
        }
    }
}

