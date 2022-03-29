package hr.fer.help193.vehicle.app.features.permissionerror.ui

import hr.fer.help193.vehicle.app.base.BaseView
import hr.fer.help193.vehicle.app.base.ViewPresenter

interface PermissionErrorContract {

    interface View : BaseView

    interface Presenter: ViewPresenter<View, PermissionErrorViewState> {

        fun requestPermission()
    }
}
