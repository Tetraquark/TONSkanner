package ru.tetraquark.ton.explorer.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalCustomThemeDimens = staticCompositionLocalOf { CustomThemeDimens() }

@Immutable
data class CustomThemeDimens(
    val xxSmall: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val xLarge: Dp,
    val xxLarge: Dp,
    val maxButtonWidthDesktop: Dp
)

fun CustomThemeDimens(): CustomThemeDimens = CustomThemeDimens(
    xxSmall = 4.dp,
    small = 8.dp,
    medium = 16.dp,
    large = 20.dp,
    xLarge = 24.dp,
    xxLarge = 32.dp,
    maxButtonWidthDesktop = 256.dp
)
