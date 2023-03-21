package ru.tetraquark.ton.explorer.lib.entryfield

import kotlinx.coroutines.flow.StateFlow

interface EntryField<V, E : Any> {
    val valueState: StateFlow<V>

    val errorState: StateFlow<E?>

    val isEnabled: StateFlow<Boolean>

    fun setError(error: E?)

    fun setValue(value: V)

    fun setIsEnabled(isEnabled: Boolean)
}
