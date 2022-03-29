package hr.fer.help193.vehicle.app.activity.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.base.BaseActivity
import hr.fer.help193.vehicle.app.permission.PermissionManagement
import hr.fer.help193.vehicle.core.navigation.MAIN_ROUTER_DISPATCHER
import hr.fer.help193.vehicle.core.navigation.router.RoutingActionConsumer
import hr.fer.help193.vehicle.core.navigation.router.RoutingActionsSource
import hr.fer.help193.vehicle.core.navigation.router.main.MainRouter
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class MainActivity : BaseActivity(), RoutingActionConsumer<MainRouter> {

    private val router: MainRouter by inject(parameters = { parametersOf(this) })
    private val routingActionsSource: RoutingActionsSource<MainRouter> by inject(named(MAIN_ROUTER_DISPATCHER))
    private val permissionManagement: PermissionManagement by inject(parameters = { parametersOf(this) })

    override fun getScopeName(): String = MAIN_ACTIVITY_SCOPE
    override fun getLayoutResource(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setKioskPolicies()
        router.showKiosk()
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>,
                                            @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            setKioskPolicies()
            router.showMap()
        } else {
            showErrorState()
        }
    }

    private fun setKioskPolicies() {
        startLockTask()
        setImmersiveMode()
    }

    private fun setImmersiveMode() {
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        window.decorView.systemUiVisibility = flags
    }

    fun showErrorState() {
        router.showErrorState()
    }

    fun showMap() {
        router.showMap()
    }

    override fun onStart() {
        super.onStart()
        routingActionsSource.setActiveRoutingActionConsumer(this)
    }

    public override fun onStop() {
        routingActionsSource.unsetRoutingActionConsumer(this)
        super.onStop()
    }

    override fun onRoutingAction(routingAction: (MainRouter) -> Unit) = routingAction(router)

    fun checkLocationPermission() {
        permissionManagement.checkLocationPermission()
    }
}
