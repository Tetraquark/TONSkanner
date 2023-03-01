package ru.tetraquark.ton.explorer.features.base.viewmodel

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import ru.tetraquark.ton.explorer.features.base.coroutines.getUiCoroutineContext
import kotlin.coroutines.CoroutineContext

abstract class ViewModelBase(
    coroutineContext: CoroutineContext = getUiCoroutineContext()
) : InstanceKeeper.Instance {
    val instanceScope: CoroutineScope = CoroutineScope(coroutineContext)

    override fun onDestroy() {
        instanceScope.cancel()
    }
}
