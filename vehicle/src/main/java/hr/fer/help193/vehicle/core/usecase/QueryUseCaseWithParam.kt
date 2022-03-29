package hr.fer.help193.vehicle.core.usecase

import io.reactivex.Flowable

interface QueryUseCaseWithParam<Param, Result> {

    operator fun invoke(param: Param): Flowable<Result>
}
