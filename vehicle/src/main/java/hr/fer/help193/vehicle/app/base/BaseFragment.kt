package hr.fer.help193.vehicle.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import hr.fer.help193.vehicle.core.navigation.router.BackPropagatingFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

abstract class BaseFragment<ViewState> : Fragment(), BaseView, BackPropagatingFragment {

    val scopeRetainer by lazy {
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = ScopeRetainer(getKoin()) as T
        }).get(ScopeRetainer::class.java)
    }

    private var disposables = CompositeDisposable()
    private lateinit var presenter: ViewPresenter<BaseView, ViewState>

    @LayoutRes
    protected abstract fun getLayoutResource(): Int

    protected abstract fun getScopeName(): String
    protected abstract fun getViewPresenter(): ViewPresenter<*, *>
    protected abstract fun render(viewState: ViewState)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scopeRetainer.setScope(getScopeName())

        presenter = getViewPresenter() as ViewPresenter<BaseView, ViewState>

        scopeRetainer.addDestroyable(presenter)
    }

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(getLayoutResource(), container, false)

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposables = CompositeDisposable()
        initialiseView(view, savedInstanceState)
        presenter.viewAttached(this)
        addDisposable(presenter.viewState().subscribe(this::render))
    }

    override fun onDestroyView() {
        presenter.viewDetached()
        disposables.dispose()
        super.onDestroyView()
    }

    override fun back() = false

    /**
     * Inject lazily given dependency from current scope
     * @param qualifier - optional bean qualifier
     * @param parameters - optional injection parameters
     */
    protected inline fun <reified T> scopedInject(
            qualifier: Qualifier? = null,
            noinline parameters: ParametersDefinition? = null
    ): Lazy<T> = lazy { scopeRetainer.getScope().get<T>(qualifier = qualifier, parameters = parameters) }


    /**
     * Override to initialise view
     */
    protected open fun initialiseView(view: View, savedInstanceState: Bundle?) {
        // Template
    }

    /**
     * Override to observe view state
     */
    protected open fun observeViewState() {
        // Template
    }

    protected fun addDisposable(disposable: Disposable) {
        if (disposables.isDisposed) {
            throw UnsupportedOperationException("View disposables are disposed")
        }

        disposables.add(disposable)
    }
}
