package ru.tetraquark.ton.explorer.features.base.coroutines

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual var getUiCoroutineContext: () -> CoroutineContext = { Dispatchers.Main }
