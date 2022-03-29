package hr.fer.help193.vehicle.utils

interface SharedPrefsUtils {

    fun getUsername(): String

    fun getPassword(): String

    fun getTabletId(): Int

    fun setCredentials(username: String, password: String, tabletId: Int)

    fun setIntervention(interventionId: Int?, vehicleId: Int?)

    fun getInterventionId(): Int

    fun getVehicleId(): Int
}
