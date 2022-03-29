package hr.fer.help193.vehicle.core.usecase

import io.reactivex.Flowable

interface QueryUseCase<Result> {

    operator fun invoke(): Flowable<Result>
}
