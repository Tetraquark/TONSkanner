package ru.tetraquark.ton.explorer.features.base.decompose

import com.arkivanov.decompose.value.MutableValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun <T : Any> StateFlow<T>.asValue(scope: CoroutineScope): MutableValue<T> {
    val value = MutableValue(this.value)
    scope.launch {
        collect { value.value = it }
    }
    return value
}
