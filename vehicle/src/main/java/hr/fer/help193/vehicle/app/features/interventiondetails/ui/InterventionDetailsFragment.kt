package hr.fer.help193.vehicle.app.features.interventiondetails.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.base.BaseFragment
import hr.fer.help193.vehicle.app.features.interventiondetails.di.INTERVENTION_DETAILS_SCOPE
import kotlinx.android.synthetic.main.fragment_intervention_details.*

class InterventionDetailsFragment : BaseFragment<InterventionDetailsViewState>(), InterventionDetailsContract.View {

    companion object {
        @LayoutRes
        val LAYOUT_RESOURCE = R.layout.fragment_intervention_details

        const val TAG = "InterventionDetailsFragment"

        fun newInstance(): Fragment = InterventionDetailsFragment()
    }

    private val presenter: InterventionDetailsContract.Presenter by scopedInject()

    override fun getLayoutResource() = LAYOUT_RESOURCE
    override fun getScopeName() = INTERVENTION_DETAILS_SCOPE
    override fun getViewPresenter() = presenter

    override fun initialiseView(view: View, savedInstanceState: Bundle?) {
        back_button.setOnClickListener { presenter.returnToMap() }
    }

    override fun render(viewState: InterventionDetailsViewState) {
        intervention_id_content.text = viewState.intervention.id?.toString() ?: resources.getString(R.string.unavailable)
        intervention_settlement_content.text = viewState.intervention.settlement ?: resources.getString(R.string.unavailable)
        intervention_neighbourhood_content.text = viewState.intervention.neighborhood ?: resources.getString(R.string.unavailable)
        intervention_districtOffice_content.text = viewState.intervention.districtOffice ?: resources.getString(R.string.unavailable)
        intervention_street_content.text = viewState.intervention.street ?: resources.getString(R.string.unavailable)
        intervention_streetWithDetails_content.text = viewState.intervention.streetWithDetails ?: resources.getString(R.string.unavailable)
        intervention_houseNumber_content.text = if (viewState.intervention.houseNumber.trim()  == "") resources.getString(R.string.unavailable) else viewState.intervention.houseNumber
        intervention_hydrantInfo_content.text = viewState.intervention.hydrantInfo ?: resources.getString(R.string.unavailable)
        intervention_interventionType_content.text = viewState.intervention.interventionTypeName ?: resources.getString(R.string.unavailable)
        intervention_event_content.text = if (viewState.intervention.event.trim() == "") resources.getString(R.string.unavailable) else viewState.intervention.event
        intervention_eventType_content.text = if (viewState.intervention.eventType.trim()  == "") resources.getString(R.string.unavailable) else viewState.intervention.eventType
        intervention_description_content.text = viewState.intervention.description ?: resources.getString(R.string.unavailable)
        intervention_importance_content.text = viewState.intervention.interventionImportanceName ?: resources.getString(R.string.unavailable)
    }
}
