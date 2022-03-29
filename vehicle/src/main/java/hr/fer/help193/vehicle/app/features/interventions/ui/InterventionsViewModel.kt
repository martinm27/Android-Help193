package hr.fer.help193.vehicle.app.features.interventions.ui

import hr.fer.help193.vehicle.utils.DiffUtilViewModel

sealed class InterventionsViewModel(override val id: Int) : DiffUtilViewModel(id)

data class InterventionViewModel(
        val interventionId: Int,
        val date: String,
        val time: String,
        val eventType: String,
        val areaOffice: String,
        val neighbourhood: String,
        val address: String,
        val description: String,
        var isSelected: Boolean
) : InterventionsViewModel(interventionId)

data class EmptyInterventionListViewModel(override val id: Int) : InterventionsViewModel(id)
