package hr.fer.help193.vehicle.app.di

import android.os.Looper
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.qualifier.named
import org.koin.dsl.module
import timber.log.Timber
import java.util.concurrent.TimeUnit

const val SERIAL_BACKGROUND_SCHEDULER = "SERIAL_BACKGROUND_SCHEDULER"
const val BACKGROUND_SCHEDULER = "BACKGROUND_SCHEDULER"
const val MAIN_SCHEDULER = "MAIN_SCHEDULER"

val ThreadingModule = module {

    single(named(SERIAL_BACKGROUND_SCHEDULER)) { Schedulers.single() }

    single(named(BACKGROUND_SCHEDULER)) { Schedulers.io() }

    single<Scheduler>(named(MAIN_SCHEDULER)) { OnRescheduleNotifyMainScheduler() }

}

private class OnRescheduleNotifyMainScheduler : Scheduler() {

    private val mainScheduler = AndroidSchedulers.from(Looper.getMainLooper(), true)

    override fun createWorker() = object : Worker() {

        private val worker = mainScheduler.createWorker()

        override fun schedule(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Timber.e(IllegalStateException("RxChain already on MainThread!"))
            }

            return worker.schedule(run, delay, unit)
        }

        override fun dispose() = worker.dispose()

        override fun isDisposed() = worker.isDisposed
    }
}
