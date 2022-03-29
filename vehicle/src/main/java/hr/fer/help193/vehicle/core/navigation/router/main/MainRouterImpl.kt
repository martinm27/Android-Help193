package hr.fer.help193.vehicle.core.navigation.router.main

import android.app.Activity
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.activity.main.MainActivity
import hr.fer.help193.vehicle.app.features.interventiondetails.ui.InterventionDetailsFragment
import hr.fer.help193.vehicle.app.features.interventions.ui.InterventionsFragment
import hr.fer.help193.vehicle.app.features.kiosk.ui.KioskFragment
import hr.fer.help193.vehicle.app.features.map.ui.MapFragment
import hr.fer.help193.vehicle.app.features.permissionerror.ui.PermissionErrorFragment
import hr.fer.help193.vehicle.app.features.popup.PopupMessage
import hr.fer.help193.vehicle.app.features.popup.ui.PopupMessageFragment
import hr.fer.help193.vehicle.app.features.vehicles.ui.VehiclesFragment
import hr.fer.help193.vehicle.utils.*

@IdRes
private const val CONTENT_CONTAINER_ID = R.id.content_fragmentContainer

@IdRes
private const val POPUP_CONTAINER_ID = R.id.main_popupContainer

private const val ROUTING_ACTION_THROTTLE_WINDOW = 300

class MainRouterImpl(private val activity: Activity,
                     private val fragmentManager: FragmentManager) : MainRouter {

    private var lastActionStamp = 0L

    override fun showKiosk() {
        fragmentManager.inTransaction {
            applyFadeInEnterAndFadeOutExitAnimation()
            replace(CONTENT_CONTAINER_ID, KioskFragment.newInstance(), KioskFragment.TAG)
        }
    }

    override fun showErrorState() {
        fragmentManager.inTransactionAndAddToBackStack {
            applyFadeInEnterAndFadeOutExitAnimation()
            add(CONTENT_CONTAINER_ID, PermissionErrorFragment.newInstance(), PermissionErrorFragment.TAG)
        }
    }

    override fun startNavigation() = (activity as MainActivity).checkLocationPermission()

    override fun showMap() {
        if (fragmentManager.findFragmentByTag(PermissionErrorFragment.TAG) != null) {
            fragmentManager.popBackStack()
        }
        fragmentManager.inTransactionAndAddToBackStack {
            applyFadeInEnterAndFadeOutExitAnimation()
            add(CONTENT_CONTAINER_ID, MapFragment.newInstance(), MapFragment.TAG)
        }
    }

    override fun showPopupMessage(popupMessage: PopupMessage) {
        fragmentManager.inTransaction {
            applyFadeInEnterAnimation()
            add(POPUP_CONTAINER_ID, PopupMessageFragment.newInstance(popupMessage), PopupMessageFragment.TAG)
        }
    }

    override fun closePopup(): Boolean {
        fragmentManager.findFragmentByTag(PopupMessageFragment.TAG)?.let {
            fragmentManager.inTransaction {
                applyFadeOutExitAnimation()
                remove(it)
            }
            return true
        }

        return false
    }

    override fun showAllInterventions() {
        fragmentManager.inTransactionAndAddToBackStack {
            applyBottomSlideEnterAndExitAnimation()
            add(CONTENT_CONTAINER_ID, InterventionsFragment.newInstance(), InterventionsFragment.TAG)
        }
    }

    override fun showAllVehicles(interventionId: Int) {
        fragmentManager.inTransactionAndAddToBackStack {
            applyBottomSlideEnterAndExitAnimation()
            add(CONTENT_CONTAINER_ID, VehiclesFragment.newInstance(interventionId), VehiclesFragment.TAG)
        }
    }

    override fun showInterventionDetails() {
        fragmentManager.inTransactionAndAddToBackStack {
            applyBottomSlideEnterAndExitAnimation()
            add(CONTENT_CONTAINER_ID, InterventionDetailsFragment.newInstance(), InterventionDetailsFragment.TAG)
        }
    }

    override fun goBackThrottled() = withThrottle {
        goBack()
    }

    override fun goBack() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        }
    }

    override fun returnToKiosk() {
        while (fragmentManager.backStackEntryCount != 0)
            fragmentManager.popBackStackImmediate()
    }

    override fun returnToMap() {
        while (fragmentManager.backStackEntryCount != 1)
            fragmentManager.popBackStackImmediate()
    }

    private fun withThrottle(action: () -> Unit) {
        System.currentTimeMillis().let {
            if (lastActionStamp < it - ROUTING_ACTION_THROTTLE_WINDOW) {
                lastActionStamp = it
                action()
            }
        }
    }

    override fun leaveApp() {
        activity.finishAndRemoveTask()
    }
}
