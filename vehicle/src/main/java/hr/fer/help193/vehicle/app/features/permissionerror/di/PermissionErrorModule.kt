package hr.fer.help193.vehicle.app.features.permissionerror.di

import hr.fer.help193.vehicle.app.di.BACKGROUND_SCHEDULER
import hr.fer.help193.vehicle.app.di.MAIN_SCHEDULER
import hr.fer.help193.vehicle.app.features.permissionerror.ui.PermissionErrorContract
import hr.fer.help193.vehicle.app.features.permissionerror.ui.PermissionErrorPresenter
import hr.fer.help193.vehicle.core.navigation.MAIN_ROUTER_DISPATCHER
import org.koin.core.qualifier.named
import org.koin.dsl.module

val PermissionErrorModule = module {

    scope(named(PERMISSION_ERROR_SCOPE)) {

        scoped<PermissionErrorContract.Presenter> {
            PermissionErrorPresenter(get()).apply {
                mainThreadScheduler = get(named(MAIN_SCHEDULER))
                backgroundScheduler = get(named(BACKGROUND_SCHEDULER))
                routingActionsDispatcher = get(named(MAIN_ROUTER_DISPATCHER))
                start()
            }
        }
    }
}

const val PERMISSION_ERROR_SCOPE = "Permission Error Scope"
