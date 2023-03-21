package ru.tetraquark.ton.explorer.features.base.coroutines

import kotlinx.coroutines.CoroutineDispatcher

object DefaultDispatchers {
    private val defaultCoroutineDispatcher = defaultCoroutineDispatcherProvider()

    val Main: CoroutineDispatcher = defaultCoroutineDispatcher.getUiCoroutineDispatcher()
    val Background: CoroutineDispatcher = defaultCoroutineDispatcher.getDefaultCoroutineDispatcher()
    val IO: CoroutineDispatcher = defaultCoroutineDispatcher.getIOCoroutineDispatcher()
}
