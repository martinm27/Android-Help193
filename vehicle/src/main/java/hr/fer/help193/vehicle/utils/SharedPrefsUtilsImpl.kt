package hr.fer.help193.vehicle.utils

import android.content.Context

class SharedPrefsUtilsImpl(private val context: Context) : SharedPrefsUtils {

    private val sharedPrefs by lazy { context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE) }

    override fun getPassword(): String = sharedPrefs.getString(KEY_AUTHENTICATION_PASSWORD, "")

    override fun getTabletId(): Int = sharedPrefs.getInt(KEY_TABLET_ID, -1)

    override fun getUsername(): String = sharedPrefs.getString(KEY_AUTHENTICATION_USERNAME, "")

    override fun setCredentials(username: String, password: String, tabletId: Int) {
        sharedPrefs.edit()
                .putString(KEY_AUTHENTICATION_USERNAME, username)
                .putString(KEY_AUTHENTICATION_PASSWORD, password)
                .putInt(KEY_TABLET_ID, tabletId)
                .apply()
    }

    override fun setIntervention(interventionId: Int?, vehicleId: Int?) {
        if (interventionId != null && vehicleId != null) {
            sharedPrefs.edit()
                    .putInt(KEY_INTERVENTION_ID, interventionId)
                    .putInt(KEY_VEHICLE_ID, vehicleId)
                    .apply()
        }
    }

    override fun getVehicleId(): Int = -1

    override fun getInterventionId(): Int = -1
}
