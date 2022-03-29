package hr.fer.help193.vehicle.app.activity.onboarding

import android.os.Bundle
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.base.BaseActivity
import hr.fer.help193.vehicle.core.navigation.ONBOARDING_ROUTER_DISPATCHER
import hr.fer.help193.vehicle.core.navigation.router.RoutingActionConsumer
import hr.fer.help193.vehicle.core.navigation.router.RoutingActionsSource
import hr.fer.help193.vehicle.core.navigation.router.onboarding.OnboardingRouter
import hr.fer.help193.vehicle.utils.KEY_AUTHENTICATION_PASSWORD
import hr.fer.help193.vehicle.utils.KEY_AUTHENTICATION_USERNAME
import hr.fer.help193.vehicle.utils.KEY_TABLET_ID
import hr.fer.help193.vehicle.utils.SharedPrefsUtils
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class OnboardingActivity : BaseActivity(), RoutingActionConsumer<OnboardingRouter>, KoinComponent {

    private val router: OnboardingRouter by inject(parameters = { parametersOf(this) })
    private val routingActionsSource: RoutingActionsSource<OnboardingRouter> by inject(named(ONBOARDING_ROUTER_DISPATCHER))
    private val sharedPrefs: SharedPrefsUtils by inject()

    override fun getScopeName(): String = ONBOARDING_ACTIVITY_SCOPE
    override fun getLayoutResource(): Int = R.layout.activity_onboarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            if (sharedPrefs.getPassword().isEmpty()) {
                router.showOnboarding()
            } else {
                setProperties()
                router.startMain()
            }
        }
    }

    private fun setProperties() {
        getKoin().setProperty(KEY_AUTHENTICATION_USERNAME, sharedPrefs.getUsername())
        getKoin().setProperty(KEY_AUTHENTICATION_PASSWORD, sharedPrefs.getPassword())
        getKoin().setProperty(KEY_TABLET_ID, sharedPrefs.getTabletId())
    }

    fun saveCredentials(username: String, password: String, tabletId: Int) {
        sharedPrefs.setCredentials(username, password, tabletId)
        setProperties()
        router.startMain()
    }

    override fun onRoutingAction(routingAction: (OnboardingRouter) -> Unit) = routingAction(router)

    override fun onStart() {
        super.onStart()
        routingActionsSource.setActiveRoutingActionConsumer(this)
    }

    public override fun onStop() {
        routingActionsSource.unsetRoutingActionConsumer(this)
        super.onStop()
    }

    override fun onBackPressed() {
        // Do nothing
    }
}
