package hr.fer.help193.vehicle.app.features.vehicles.ui

import hr.fer.help193.vehicle.utils.DiffUtilViewModel

sealed class StationVehicleViewModel(override val id: Int) : DiffUtilViewModel()

data class VehicleViewModel(override val id: Int,
                            val name: String,
                            val isSelected: Boolean,
                            val stationColor: String
) : StationVehicleViewModel(id)

data class StationViewModel(override val id: Int,
                            val name: String?
) : StationVehicleViewModel(id)
