package ru.tetraquark.ton.explorer.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val LocalCustomThemeTypography = staticCompositionLocalOf { CustomThemeTypography() }

@Immutable
data class CustomThemeTypography(
    val normal14: TextStyle,
    val medium14: TextStyle,
    val semiBold14: TextStyle,
    val normal16: TextStyle,
    val medium16: TextStyle,
    val normal20: TextStyle,
    val medium20: TextStyle,
    val semiBold16: TextStyle,
    val semiBold32: TextStyle,
    val button: TextStyle
)

fun CustomThemeTypography(): CustomThemeTypography {
    val normal = FontWeight.Normal
    val medium = FontWeight.Medium
    val semiBold = FontWeight.SemiBold

    val textBodySize = 14.sp
    val textTitleSize = 20.sp
    val textLargeTitleSize = 32.sp
    val textSubTitleSize = 16.sp

    return CustomThemeTypography(
        normal14 = TextStyle(
            fontSize = textBodySize,
            fontWeight = normal,
            lineHeight = 16.sp
        ),
        medium14 = TextStyle(
            fontSize = textBodySize,
            fontWeight = medium
        ),
        semiBold14 = TextStyle(
            fontSize = textBodySize,
            fontWeight = semiBold
        ),
        normal16 = TextStyle(
            fontSize = textSubTitleSize,
            fontWeight = normal
        ),
        medium16 = TextStyle(
            fontSize = textSubTitleSize,
            fontWeight = medium
        ),
        semiBold16 = TextStyle(
            fontSize = textSubTitleSize,
            fontWeight = semiBold
        ),
        normal20 = TextStyle(
            fontSize = textTitleSize,
            fontWeight = normal
        ),
        medium20 = TextStyle(
            fontSize = textTitleSize,
            fontWeight = medium
        ),
        semiBold32 = TextStyle(
            fontSize = textLargeTitleSize,
            fontWeight = semiBold,
            lineHeight = 38.sp
        ),
        button = TextStyle(
            fontWeight = medium,
            fontSize = textBodySize,
            letterSpacing = 1.25.sp
        )
    )
}
