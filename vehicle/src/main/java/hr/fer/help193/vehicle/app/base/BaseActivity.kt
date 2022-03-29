package hr.fer.help193.vehicle.app.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

abstract class BaseActivity : AppCompatActivity() {

    val scopeRetainer by lazy {
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = ScopeRetainer(getKoin()) as T
        }).get(ScopeRetainer::class.java)
    }

    protected abstract fun getScopeName(): String

    @LayoutRes
    protected abstract fun getLayoutResource(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        scopeRetainer.setScope(getScopeName())
    }

    /**
     * Inject lazily given dependency from current scope
     * @param qualifier - optional bean qualifier
     * @param parameters - optional injection parameters
     */
    protected inline fun <reified T> scopedInject(
            qualifier: Qualifier? = null,
            noinline parameters: ParametersDefinition? = null
    ): Lazy<T> = lazy { scopeRetainer.getScope().get<T>(qualifier = qualifier, parameters = parameters) }
}
