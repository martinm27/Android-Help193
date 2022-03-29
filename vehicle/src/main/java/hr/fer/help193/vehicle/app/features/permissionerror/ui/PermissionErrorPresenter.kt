package hr.fer.help193.vehicle.app.features.permissionerror.ui

import hr.fer.help193.vehicle.app.base.BasePresenter
import hr.fer.help193.vehicle.app.permission.PermissionManagement

class PermissionErrorPresenter(private val permissionManagement: PermissionManagement) : BasePresenter<PermissionErrorContract.View, PermissionErrorViewState>(), PermissionErrorContract.Presenter {

    override fun initialViewState(): PermissionErrorViewState = PermissionErrorViewState()

    override fun requestPermission() = permissionManagement.requestPermission()
}
