package ru.tetraquark.ton.explorer.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import dev.icerock.moko.resources.desc.image.ImageDesc
import dev.icerock.moko.resources.desc.image.asImageDesc
import ru.tetraquark.ton.explorer.app.ui.MR

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

fun defaultCustomThemeDrawables(): CustomThemeDrawables = CustomThemeDrawables(
    arrowUp = MR.images.ic_arrow_up.asImageDesc(),
    arrowDown = MR.images.ic_arrow_down.asImageDesc(),
    arrowLeft = MR.images.ic_arrow_left.asImageDesc(),
    arrowRight = MR.images.ic_arrow_right.asImageDesc(),
    keyboardArrowRight = MR.images.ic_keyboard_arrow_right.asImageDesc(),
    search = MR.images.ic_search.asImageDesc(),
    copy = MR.images.ic_copy.asImageDesc(),
    gmailErrorred = MR.images.ic_report_gmailerrorred.asImageDesc(),
    sentimentVeryDissatisfied = MR.images.ic_sentiment_very_dissatisfied.asImageDesc(),
    list = MR.images.ic_list.asImageDesc(),
    image = MR.images.ic_image.asImageDesc(),
    tokens = MR.images.ic_tokens.asImageDesc()
)
