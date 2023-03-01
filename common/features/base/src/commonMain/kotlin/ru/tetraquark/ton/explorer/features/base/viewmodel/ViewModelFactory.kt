package ru.tetraquark.ton.explorer.features.base.viewmodel

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy

inline fun <reified T : InstanceKeeper.Instance> LifecycleOwner.viewModel(
    instanceKeeper: InstanceKeeper,
    key: Any,
    factory: () -> T,
): T {
    return instanceKeeper.getOrCreate(key, factory).also {
        lifecycle.doOnDestroy(it::onDestroy)
    }
}

inline fun <reified T : InstanceKeeper.Instance> LifecycleOwner.viewModel(
    instanceKeeper: InstanceKeeper,
    factory: () -> T,
): T = viewModel(instanceKeeper, T::class, factory)
