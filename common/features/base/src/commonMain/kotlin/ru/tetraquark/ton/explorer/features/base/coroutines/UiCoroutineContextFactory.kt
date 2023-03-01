package ru.tetraquark.ton.explorer.features.base.coroutines

import kotlin.coroutines.CoroutineContext

expect var getUiCoroutineContext: () -> CoroutineContext
