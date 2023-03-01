package ru.tetraquark.ton.explorer.lib.entryfield

fun interface ValidationRule<V, E> {
    fun validate(value: V): E?
}
