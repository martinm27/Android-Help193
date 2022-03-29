package hr.fer.help193.vehicle.app.features.map.di

import hr.fer.help193.vehicle.app.di.BACKGROUND_SCHEDULER
import hr.fer.help193.vehicle.app.di.MAIN_SCHEDULER
import hr.fer.help193.vehicle.app.features.map.translations.MapTranslations
import hr.fer.help193.vehicle.app.features.map.translations.MapTranslationsImpl
import hr.fer.help193.vehicle.app.features.map.ui.MapContract
import hr.fer.help193.vehicle.app.features.map.ui.MapPresenter
import hr.fer.help193.vehicle.app.features.map.usecase.*
import hr.fer.help193.vehicle.core.navigation.MAIN_ROUTER_DISPATCHER
import org.koin.core.qualifier.named
import org.koin.dsl.module

val MapModule = module {

    scope(named(MAP_SCOPE)) {

        scoped<MapContract.Presenter> {
            MapPresenter(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()).apply {
                mainThreadScheduler = get(named(MAIN_SCHEDULER))
                backgroundScheduler = get(named(BACKGROUND_SCHEDULER))
                routingActionsDispatcher = get(named(MAIN_ROUTER_DISPATCHER))
                start()
            }
        }

        single { QueryNewInterventions(get()) }
        single { QueryHydrants(get()) }
        single { UpdatePosition(get(), get()) }
        single { QueryRouteDescription(get()) }
        single { QueryTabletData(get()) }
        single<MapTranslations> { MapTranslationsImpl(get()) }
    }
}

const val MAP_SCOPE = "Map Scope"
