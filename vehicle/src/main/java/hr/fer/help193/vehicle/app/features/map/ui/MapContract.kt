package hr.fer.help193.vehicle.app.features.map.ui

import hr.fer.help193.vehicle.app.base.BaseView
import hr.fer.help193.vehicle.app.base.ViewPresenter
import org.locationtech.jts.geom.Point

interface MapContract {

    interface View : BaseView

    interface Presenter : ViewPresenter<View, MapViewState> {

        fun returnToKiosk()

        fun chooseIntervention()

        fun updatePosition(point: Point, speed: Double, heading: Double)

        fun removeIntervention()

        fun showDetails()
    }
}
