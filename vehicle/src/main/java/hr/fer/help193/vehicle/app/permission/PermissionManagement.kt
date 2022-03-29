package hr.fer.help193.vehicle.app.permission

interface PermissionManagement {

    fun checkLocationPermission()

    fun requestPermission()

    companion object {
        const val REQUEST_CODE_PERMISSION = 222
        const val REQUEST_CODE_PERMISSION_ERROR = 333
    }
}
