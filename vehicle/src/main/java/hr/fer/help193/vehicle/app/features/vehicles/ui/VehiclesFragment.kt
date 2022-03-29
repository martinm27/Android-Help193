package hr.fer.help193.vehicle.app.features.vehicles.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.base.BaseFragment
import hr.fer.help193.vehicle.app.features.vehicles.di.VEHICLES_SCOPE
import hr.fer.help193.vehicle.utils.ALPHA_HALF
import hr.fer.help193.vehicle.utils.ALPHA_OPAQUE
import hr.fer.help193.vehicle.app.view.GridItemSpacingDecoration
import kotlinx.android.synthetic.main.fragment_vehicles.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class VehiclesFragment : BaseFragment<VehiclesViewState>(), VehiclesContract.View {

    companion object {
        const val LAYOUT = R.layout.fragment_vehicles
        const val TAG: String = "VehiclesFragment"

        private const val KEY_INTERVENTION_ID = "key_intervention_id"

        fun newInstance(interventionId: Int): Fragment = VehiclesFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_INTERVENTION_ID, interventionId)
            }
        }
    }

    private val stationGridSpanCount: Int by lazy { resources.getInteger(R.integer.station_grid_span_size) }
    private val defaultGridSpanCount: Int by lazy { resources.getInteger(R.integer.vehicles_grid_span_size) }
    private val presenter: VehiclesContract.Presenter by scopedInject(parameters = { parametersOf(arguments!!.getInt(KEY_INTERVENTION_ID)) })
    private val vehiclesAdapter: VehiclesAdapter by inject(parameters = { parametersOf(layoutInflater, presenter::onSelectedChanged) })

    override fun getLayoutResource(): Int = LAYOUT
    override fun getScopeName(): String = VEHICLES_SCOPE
    override fun getViewPresenter() = presenter

    override fun initialiseView(view: View, savedInstanceState: Bundle?) {
        vehicles_back.setOnClickListener { presenter.returnToInterventions() }
        vehicles_confirm.setOnClickListener { presenter.chooseVehicle() }
        with(vehicles_recyclerView) {
            layoutManager = getGridLayoutManager()
            context?.let {
                addItemDecoration(get<GridItemSpacingDecoration>(parameters = { parametersOf(it) }))
            }
            adapter = vehiclesAdapter
            (itemAnimator as SimpleItemAnimator).run {
                supportsChangeAnimations = false
            }
        }
    }

    private fun getGridLayoutManager(): RecyclerView.LayoutManager =
            get<GridLayoutManager>(parameters = { parametersOf(context!!, R.integer.vehicles_grid_count) })
                    .apply {
                        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int =
                                    when (vehiclesAdapter.getItemViewType(position)) {
                                        VehiclesAdapter.ItemViewType.STATION.ordinal -> stationGridSpanCount
                                        else -> defaultGridSpanCount
                                    }
                        }
                    }

    override fun render(viewState: VehiclesViewState) {
        val stationVehicleViewModels = ArrayList<StationVehicleViewModel>()

        for (stationViewModel in viewState.vehicles.keys) {
            stationVehicleViewModels.add(stationViewModel)

            viewState.vehicles[stationViewModel]?.let {
                stationVehicleViewModels.addAll(it)
            }
        }
        vehicles_confirm.alpha = stationVehicleViewModels.find {
            if (it is VehicleViewModel) {
                it.isSelected
            } else {
                false
            }
        }?.let {
            ALPHA_OPAQUE
        } ?: ALPHA_HALF
        vehiclesAdapter.submitList(stationVehicleViewModels)
    }
}

