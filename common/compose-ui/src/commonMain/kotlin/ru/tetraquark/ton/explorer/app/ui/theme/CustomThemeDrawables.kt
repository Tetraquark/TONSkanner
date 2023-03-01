package ru.tetraquark.ton.explorer.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import dev.icerock.moko.resources.desc.image.ImageDesc

val LocalCustomThemeDrawables = compositionLocalOf<CustomThemeDrawables> {
    error("There is no CustomThemeDrawables providers")
}

@Immutable
data class CustomThemeDrawables(
    val arrowUp: ImageDesc,
    val arrowDown: ImageDesc,
    val arrowLeft: ImageDesc,
    val arrowRight: ImageDesc,
    val keyboardArrowRight: ImageDesc,
    val search: ImageDesc,
    val copy: ImageDesc,
    val gmailErrorred: ImageDesc,
    val sentimentVeryDissatisfied: ImageDesc,
    val list: ImageDesc,
    val image: ImageDesc,
    val tokens: ImageDesc,
)
