package hr.fer.help193.vehicle.app.activity.onboarding

import hr.fer.help193.vehicle.data.network.WebSocketPairingService
import hr.fer.help193.vehicle.app.di.createOkHttpClient
import hr.fer.help193.vehicle.app.di.createWebSocketService
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val WEB_SOCKET_PAIRING_URL = "wss://applygit.zvne.fer.hr/vatrogasci/be/ws/tablet-registration"
private const val ONBOARDING_HTTP_CLIENT = "OnboardingHttpClient"

val OnboardingActivityModule = module {

    scope(named(ONBOARDING_ACTIVITY_SCOPE)) {
        single(named(ONBOARDING_HTTP_CLIENT)) { createOkHttpClient() }
        single { createWebSocketService<WebSocketPairingService>(get(named(ONBOARDING_HTTP_CLIENT)), WEB_SOCKET_PAIRING_URL, get(), androidApplication()) }
    }
}

const val ONBOARDING_ACTIVITY_SCOPE = "Onboarding activity scope"
