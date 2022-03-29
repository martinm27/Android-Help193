package hr.fer.help193.vehicle.app.permission

import android.Manifest
import android.content.DialogInterface.OnClickListener
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.FragmentActivity
import hr.fer.help193.vehicle.app.activity.main.MainActivity
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.permission.PermissionManagement.Companion.REQUEST_CODE_PERMISSION
import hr.fer.help193.vehicle.app.permission.PermissionManagement.Companion.REQUEST_CODE_PERMISSION_ERROR

class PermissionManagementImpl(private val activity: FragmentActivity,
                               private val resources: Resources) : PermissionManagement {

    override fun requestPermission() {
        requestPermissions(activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_PERMISSION_ERROR)
    }

    override fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {

                showMessageOKCancel(resources.getString(R.string.explanation), OnClickListener { dialog, which ->
                    requestPermissions(activity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                            REQUEST_CODE_PERMISSION)
                }, OnClickListener { dialog, which ->
                    (activity as MainActivity).showErrorState()
                })
            } else {
                requestPermissions(activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        REQUEST_CODE_PERMISSION)
            }
        } else {
            (activity as MainActivity).showMap()
        }
    }

    private fun showMessageOKCancel(message: String, okListener: OnClickListener, cancelListener: OnClickListener) {
        val builder = AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(resources.getString(R.string.allow), okListener)
                .setNegativeButton(resources.getString(R.string.deny), cancelListener)
        val d = builder.show()
        d.window?.setBackgroundDrawableResource(android.R.color.background_light)
    }

}
