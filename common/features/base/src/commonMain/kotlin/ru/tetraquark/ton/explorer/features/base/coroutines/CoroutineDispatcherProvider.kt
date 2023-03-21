package ru.tetraquark.ton.explorer.features.base.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatcherProvider {
    fun getUiCoroutineDispatcher(): CoroutineDispatcher

    fun getDefaultCoroutineDispatcher(): CoroutineDispatcher

    fun getIOCoroutineDispatcher(): CoroutineDispatcher
}

expect var defaultCoroutineDispatcherProvider: () -> CoroutineDispatcherProvider
