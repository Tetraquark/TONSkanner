package ru.tetraquark.ton.explorer.app.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.color.ColorDescThemed
import dev.icerock.moko.resources.desc.color.asColorDesc

@Composable
actual fun StringDesc.asString(): String = this.localized()

@Composable
actual fun ColorResource.getLightColor(): Color =
    (asColorDesc() as ColorDescThemed).lightColor.toCompose()

@Composable
actual fun ColorResource.getDarkColor(): Color =
    (asColorDesc() as ColorDescThemed).darkColor.toCompose()
