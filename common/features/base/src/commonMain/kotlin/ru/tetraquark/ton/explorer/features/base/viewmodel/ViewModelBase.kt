package ru.tetraquark.ton.explorer.features.base.viewmodel

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.CoroutineDispatcher
import ru.tetraquark.ton.explorer.features.base.coroutines.DefaultDispatchers

abstract class ViewModelBase(
    coroutineDispatcher: CoroutineDispatcher = DefaultDispatchers.Main
) : InstanceKeeper.Instance {
    val instanceScope: CoroutineScope = CoroutineScope(SupervisorJob() + coroutineDispatcher)

    override fun onDestroy() {
        instanceScope.cancel()
    }
}
