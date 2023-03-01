package ru.tetraquark.ton.explorer.app.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.px(): Float = with(LocalDensity.current) {
    toPx()
}
