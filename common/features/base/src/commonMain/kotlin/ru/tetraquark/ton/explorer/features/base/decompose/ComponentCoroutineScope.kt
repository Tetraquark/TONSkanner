package ru.tetraquark.ton.explorer.features.base.decompose

import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import ru.tetraquark.ton.explorer.features.base.coroutines.DefaultDispatchers
import kotlin.coroutines.CoroutineContext

fun CoroutineScope(context: CoroutineContext, lifecycle: Lifecycle): CoroutineScope {
    val scope = CoroutineScope(context)
    lifecycle.doOnDestroy(scope::cancel)
    return scope
}

fun LifecycleOwner.CoroutineScope(context: CoroutineContext): CoroutineScope =
    CoroutineScope(context, lifecycle)

fun LifecycleOwner.CoroutineScope(): CoroutineScope =
    CoroutineScope(DefaultDispatchers.Main, lifecycle)
