package ru.tetraquark.ton.explorer.app.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

val LocalCustomThemeShapes = staticCompositionLocalOf { CustomThemeShapes() }

@Immutable
data class CustomThemeShapes(
    val small: CornerBasedShape = RoundedCornerShape(4.dp),
    val medium: CornerBasedShape = RoundedCornerShape(4.dp),
    val large: CornerBasedShape = RoundedCornerShape(0.dp),
    val actionButton: CornerBasedShape = RoundedCornerShape(12.dp),
    val cardShape: CornerBasedShape = RoundedCornerShape(14.dp),
)
