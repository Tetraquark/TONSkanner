package ru.tetraquark.ton.explorer.features.base.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual var defaultCoroutineDispatcherProvider: () -> CoroutineDispatcherProvider =
    { CoroutineDispatcherProviderJvm() }

private class CoroutineDispatcherProviderJvm : CoroutineDispatcherProvider {
    override fun getUiCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main

    override fun getDefaultCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Default

    override fun getIOCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
