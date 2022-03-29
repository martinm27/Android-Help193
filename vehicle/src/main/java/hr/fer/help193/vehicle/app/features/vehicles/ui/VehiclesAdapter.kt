package hr.fer.help193.vehicle.app.features.vehicles.ui

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.features.vehicles.ui.VehiclesAdapter.ItemViewType.STATION
import hr.fer.help193.vehicle.app.features.vehicles.ui.VehiclesAdapter.ItemViewType.VEHICLE
import hr.fer.help193.vehicle.utils.DiffUtilCallback
import kotlinx.android.synthetic.main.item_station.view.*
import kotlinx.android.synthetic.main.item_vehicle.view.*

typealias OnSelectedChanged = (Int) -> Unit

class VehiclesAdapter(private val layoutInflater: LayoutInflater,
                      private val onSelectedChanged: OnSelectedChanged)
    : ListAdapter<StationVehicleViewModel, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    enum class ItemViewType {
        VEHICLE,
        STATION;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == STATION.ordinal) {
            StationViewHolder(layoutInflater.inflate(StationViewHolder.LAYOUT, parent, false))
        } else {
            VehicleViewHolder(layoutInflater.inflate(VehicleViewHolder.LAYOUT, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val viewModel = getItem(position)) {
            is VehicleViewModel -> (holder as VehicleViewHolder).render(viewModel, onSelectedChanged)
            else -> (holder as StationViewHolder).render((viewModel as StationViewModel))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position) is VehicleViewModel -> VEHICLE.ordinal
            else -> STATION.ordinal
        }
    }
}

class VehicleViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        const val LAYOUT = R.layout.item_vehicle
    }

    private val title: TextView by lazy { view.item_vehicleTitle }
    private var isCheckmarkSelected = false

    fun render(viewModel: VehicleViewModel, onSelectedChanged: OnSelectedChanged) {
        with(viewModel) {
            title.text = name
            isCheckmarkSelected = isSelected
            setCheckmark(stationColor)
            itemView.setOnClickListener {
                toggleCheckmark(stationColor)
                onSelectedChanged(id)
            }
        }
    }

    private fun toggleCheckmark(stationColor: String) {
        isCheckmarkSelected = !isCheckmarkSelected
        setCheckmark(stationColor)
    }

    private fun setCheckmark(stationColor: String) {
        if (isCheckmarkSelected) {
            (title.background as GradientDrawable).setColor(Color.parseColor(stationColor))
            title.setTextColor(ContextCompat.getColor(view.context, R.color.white))
        } else {
            (title.background as GradientDrawable).setColor(ContextCompat.getColor(view.context, R.color.white))
            (title.background as GradientDrawable).setStroke(5, Color.parseColor(stationColor))
            title.setTextColor(Color.parseColor(stationColor))
        }
    }
}

class StationViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        const val LAYOUT = R.layout.item_station
    }

    private val title: TextView by lazy { view.item_stationName }

    fun render(stationViewModel: StationViewModel) {
        title.text = stationViewModel.name
    }
}

