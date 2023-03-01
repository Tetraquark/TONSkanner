package ru.tetraquark.ton.explorer.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

object CustomTheme {
    val colors: CustomThemeColors
        @Composable
        get() = LocalCustomThemeColor.current
    val typography: CustomThemeTypography
        @Composable
        get() = LocalCustomThemeTypography.current
    val dimens: CustomThemeDimens
        @Composable
        get() = LocalCustomThemeDimens.current
    val shapes: CustomThemeShapes
        @Composable
        get() = LocalCustomThemeShapes.current
    val drawables: CustomThemeDrawables
        @Composable
        get() = LocalCustomThemeDrawables.current
}

@Composable
fun CustomTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    drawables: CustomThemeDrawables,
    content: @Composable () -> Unit
) {
    val colors = if (isDarkTheme) {
        DarkCustomThemeColors()
    } else {
        LightCustomThemeColors()
    }
    val typography = CustomThemeTypography()
    val dimens = CustomThemeDimens()
    val shapes = CustomThemeShapes()

    CompositionLocalProvider(
        LocalCustomThemeColor provides colors,
        LocalCustomThemeTypography provides typography,
        LocalCustomThemeDimens provides dimens,
        LocalCustomThemeShapes provides shapes,
        LocalCustomThemeDrawables provides drawables
    ) {
        content()
    }
}
