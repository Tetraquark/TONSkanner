package ru.tetraquark.ton.explorer.lib.state

interface StateWithError<E> {
    val errorEvent: E?
}
