package hr.fer.help193.vehicle.app.features.interventions.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.features.interventions.ui.InterventionsAdapter.ItemViewType.EMPTY
import hr.fer.help193.vehicle.app.features.interventions.ui.InterventionsAdapter.ItemViewType.INTERVENTION
import hr.fer.help193.vehicle.utils.DiffUtilCallback
import kotlinx.android.synthetic.main.item_intervention.view.*

typealias OnSelectedChanged = (Int) -> Unit

class InterventionsAdapter(private val layoutInflater: LayoutInflater,
                           private val onSelectedChanged: OnSelectedChanged)
    : ListAdapter<InterventionsViewModel, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    enum class ItemViewType {
        INTERVENTION,
        EMPTY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == INTERVENTION.ordinal) {
            InterventionsViewHolder(layoutInflater.inflate(InterventionsViewHolder.LAYOUT, parent, false))
        } else {
            EmptyStateViewHolder(layoutInflater.inflate(EmptyStateViewHolder.LAYOUT, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val viewModel = getItem(position)) {
            is InterventionViewModel -> (holder as InterventionsViewHolder).render(viewModel, onSelectedChanged)
            else -> (holder as EmptyStateViewHolder).render()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position) is InterventionViewModel -> INTERVENTION.ordinal
            else -> EMPTY.ordinal
        }
    }
}

class EmptyStateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        const val LAYOUT = R.layout.item_empty_state_intervention
    }

    fun render() {

    }
}

class InterventionsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val uncheckedBackground: Drawable by lazy { itemView.context.getDrawable(R.drawable.bg_white_rounded) }
    private val checkedBackground: Drawable by lazy { itemView.context.getDrawable(R.drawable.kiosk_item_gradient) }

    companion object {
        const val LAYOUT = R.layout.item_intervention
    }

    private val item: View by lazy { view.item_intervention }
    private val dateTextView: TextView by lazy { view.item_interventionDate }
    private val timeTextView: TextView by lazy { view.item_interventionTime }
    private val eventTypeTextView: TextView by lazy { view.item_interventionEventType }
    private val areaOfficeTextView: TextView by lazy { view.item_interventionAreaOffice }
    private val neighbourhoodTextView: TextView by lazy { view.item_interventionNeighbourhood }
    private val addressTextView: TextView by lazy { view.item_interventionAddress }
    private val descriptionTextView: TextView by lazy { view.item_interventionDescription }
    private var isCheckmarkSelected = false

    fun render(viewModel: InterventionViewModel, onSelectedChanged: OnSelectedChanged) {
        with(viewModel) {
            dateTextView.text = date
            timeTextView.text = time
            eventTypeTextView.text = eventType
            areaOfficeTextView.text = areaOffice
            neighbourhoodTextView.text = neighbourhood
            addressTextView.text = address
            descriptionTextView.text = description
            isCheckmarkSelected = isSelected
            setCheckmark()
            itemView.setOnClickListener {
                toggleCheckmark()
                onSelectedChanged(interventionId)
            }
        }
    }

    private fun toggleCheckmark() {
        isCheckmarkSelected = !isCheckmarkSelected
        setCheckmark()
    }

    private fun setCheckmark() {
        item.background = if (isCheckmarkSelected) checkedBackground else uncheckedBackground
        dateTextView.setTextColor(ContextCompat.getColor(view.context, if (isCheckmarkSelected) R.color.white else R.color.black))
        timeTextView.setTextColor(ContextCompat.getColor(view.context, if (isCheckmarkSelected) R.color.white else R.color.black))
        eventTypeTextView.setTextColor(ContextCompat.getColor(view.context, if (isCheckmarkSelected) R.color.white else R.color.black))
        areaOfficeTextView.setTextColor(ContextCompat.getColor(view.context, if (isCheckmarkSelected) R.color.white else R.color.black))
        neighbourhoodTextView.setTextColor(ContextCompat.getColor(view.context, if (isCheckmarkSelected) R.color.white else R.color.black))
        addressTextView.setTextColor(ContextCompat.getColor(view.context, if (isCheckmarkSelected) R.color.white else R.color.black))
        descriptionTextView.setTextColor(ContextCompat.getColor(view.context, if (isCheckmarkSelected) R.color.white else R.color.black))
    }
}
