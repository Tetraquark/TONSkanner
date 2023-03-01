package ru.tetraquark.ton.explorer.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import ru.tetraquark.ton.explorer.features.root.MR

val LocalCustomThemeColor = staticCompositionLocalOf { LightCustomThemeColors() }

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
fun LightCustomThemeColors(): CustomThemeColors {
    return CustomThemeColors(
        isDarkColors = false,
        background = MR.colors.background.light.toCompose(),
        surface = MR.colors.surface.light.toCompose(),
        outline = MR.colors.outline.light.toCompose(),
        secondary = MR.colors.secondary.light.toCompose(),
        text = MR.colors.text.light.toCompose(),
        primary = MR.colors.primary.light.toCompose(),
        onPrimary = MR.colors.onPrimary.light.toCompose(),
        primaryLight = MR.colors.primaryLight.light.toCompose(),
        primaryDark = MR.colors.primaryDark.light.toCompose(),
        primaryBackground = MR.colors.primaryBackground.light.toCompose(),
        success = MR.colors.success.light.toCompose(),
        warning = MR.colors.warning.light.toCompose(),
        error = MR.colors.error.light.toCompose(),
    )
}

@Suppress("FunctionName")
fun DarkCustomThemeColors(): CustomThemeColors {
    return CustomThemeColors(
        isDarkColors = true,
        background = MR.colors.background.dark.toCompose(),
        surface = MR.colors.surface.dark.toCompose(),
        outline = MR.colors.outline.dark.toCompose(),
        secondary = MR.colors.secondary.dark.toCompose(),
        text = MR.colors.text.dark.toCompose(),
        primary = MR.colors.primary.dark.toCompose(),
        onPrimary = MR.colors.onPrimary.dark.toCompose(),
        primaryLight = MR.colors.primaryLight.dark.toCompose(),
        primaryDark = MR.colors.primaryDark.dark.toCompose(),
        primaryBackground = MR.colors.primaryBackground.dark.toCompose(),
        success = MR.colors.success.dark.toCompose(),
        warning = MR.colors.warning.dark.toCompose(),
        error = MR.colors.error.dark.toCompose(),
    )
}

private fun dev.icerock.moko.graphics.Color.toCompose(): Color = Color(red, green, blue, alpha)
