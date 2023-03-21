package ru.tetraquark.ton.explorer.features.base.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newFixedThreadPoolContext

actual var defaultCoroutineDispatcherProvider: () -> CoroutineDispatcherProvider =
    { CoroutineDispatcherProviderIos() }

private class CoroutineDispatcherProviderIos : CoroutineDispatcherProvider {
    override fun getUiCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main

    override fun getDefaultCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Default

    override fun getIOCoroutineDispatcher(): CoroutineDispatcher =
        newFixedThreadPoolContext(64, "Dispatchers.IO")
}
