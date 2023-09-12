package ru.tetraquark.ton.explorer.app.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import ru.tetraquark.ton.explorer.app.ui.utils.getDarkColor
import ru.tetraquark.ton.explorer.app.ui.utils.getLightColor
import ru.tetraquark.ton.explorer.features.root.MR

val LocalCustomThemeColor = staticCompositionLocalOf<CustomThemeColors> {
    error("CustomTheme colors are not initialized ")
}

@Immutable
data class CustomThemeColors(
    val isDarkColors: Boolean,
    val background: Color,
    val surface: Color,
    val outline: Color,
    val secondary: Color,
    val text: Color,
    val primary: Color,
    val onPrimary: Color,
    val primaryLight: Color,
    val primaryDark: Color,
    val primaryBackground: Color,
    val success: Color,
    val warning: Color,
    val error: Color,
)

@Suppress("FunctionName")
@Composable
fun LightCustomThemeColors(): CustomThemeColors {
    return CustomThemeColors(
        isDarkColors = false,
        background = MR.colors.background.getLightColor(),
        surface = MR.colors.surface.getLightColor(),
        outline = MR.colors.outline.getLightColor(),
        secondary = MR.colors.secondary.getLightColor(),
        text = MR.colors.text.getLightColor(),
        primary = MR.colors.primary.getLightColor(),
        onPrimary = MR.colors.onPrimary.getLightColor(),
        primaryLight = MR.colors.primaryLight.getLightColor(),
        primaryDark = MR.colors.primaryDark.getLightColor(),
        primaryBackground = MR.colors.primaryBackground.getLightColor(),
        success = MR.colors.success.getLightColor(),
        warning = MR.colors.warning.getLightColor(),
        error = MR.colors.error.getLightColor(),
    )
}

@Suppress("FunctionName")
@Composable
fun DarkCustomThemeColors(): CustomThemeColors {
    return CustomThemeColors(
        isDarkColors = true,
        background = MR.colors.background.getDarkColor(),
        surface = MR.colors.surface.getDarkColor(),
        outline = MR.colors.outline.getDarkColor(),
        secondary = MR.colors.secondary.getDarkColor(),
        text = MR.colors.text.getDarkColor(),
        primary = MR.colors.primary.getDarkColor(),
        onPrimary = MR.colors.onPrimary.getDarkColor(),
        primaryLight = MR.colors.primaryLight.getDarkColor(),
        primaryDark = MR.colors.primaryDark.getDarkColor(),
        primaryBackground = MR.colors.primaryBackground.getDarkColor(),
        success = MR.colors.success.getDarkColor(),
        warning = MR.colors.warning.getDarkColor(),
        error = MR.colors.error.getDarkColor(),
    )
}
