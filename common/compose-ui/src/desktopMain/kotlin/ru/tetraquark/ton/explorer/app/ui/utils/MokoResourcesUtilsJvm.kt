package ru.tetraquark.ton.explorer.app.ui.utils

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.desc.StringDesc

@Composable
actual fun StringDesc.asString(): String = this.localized()
