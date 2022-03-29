package hr.fer.help193.vehicle.app.base

import androidx.lifecycle.ViewModel
import hr.fer.help193.vehicle.core.lifecycle.Destroyable
import hr.fer.help193.vehicle.core.lifecycle.Destroyables
import org.koin.core.Koin
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import java.util.*

class ScopeRetainer(private val koin: Koin) : ViewModel() {

    private lateinit var definedScope: String
    private lateinit var scope: Scope

    private val destroyables = Destroyables()

    fun setScope(scopeName: String) {
        if (this::definedScope.isInitialized) {
            if (definedScope != scopeName) {
                throw IllegalStateException("Invalid new scope $scopeName for scoped VM with scope $definedScope")
            }
            return
        }

        definedScope = scopeName
        scope = koin.createScope(UUID.randomUUID().toString(), named(definedScope))
    }

    fun getScope() = scope

    fun addDestroyable(destroyable: Destroyable) {
        destroyables.addDestroyable(destroyable)
    }

    override fun onCleared() {
        scope.close()
        destroyables.destroy()
    }
}
