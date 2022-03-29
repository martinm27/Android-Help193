package hr.fer.help193.vehicle.app.features.map.translations

import android.content.res.Resources
import hr.fer.help193.vehicle.R

class MapTranslationsImpl(private val resources: Resources) : MapTranslations {

    override fun interventionClosed(): String = resources.getString(R.string.intervention_closed)

    override fun fireIcon(): Int = R.drawable.ic_fire

    override fun emptyDescription(): String = resources.getString(R.string.empty_state_intervention)

    override fun emptyVehicleName(): String = resources.getString(R.string.no_name_vehicle)

    override fun newInterventionReady(interventionEvent: String): String = String.format(resources.getString(R.string.newInterventionReady), interventionEvent)

    override fun cancel(): String = resources.getString(R.string.back)

    override fun proceed(): String = resources.getString(R.string.confirm)

    override fun closingInterventionWarning(): String = resources.getString(R.string.closing_intervention_warning)
}
