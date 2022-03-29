package hr.fer.help193.vehicle.app.features.map.translations

interface MapTranslations {

    fun newInterventionReady(interventionEvent: String): String

    fun cancel(): String

    fun proceed(): String

    fun emptyDescription(): String

    fun emptyVehicleName(): String

    fun fireIcon(): Int

    fun closingInterventionWarning(): String

    fun interventionClosed(): String
}
