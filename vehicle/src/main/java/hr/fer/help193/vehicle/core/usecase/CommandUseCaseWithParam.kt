package hr.fer.help193.vehicle.core.usecase

import io.reactivex.Completable

interface CommandUseCaseWithParam<Param> {

    operator fun invoke(param: Param): Completable
}
