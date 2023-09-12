package ru.tetraquark.ton.explorer.app.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.desc.StringDesc

@Composable
actual fun StringDesc.asString(): String = this.localized()

@Composable
actual fun ColorResource.getLightColor(): Color = lightColor.toCompose()

@Composable
actual fun ColorResource.getDarkColor(): Color = darkColor.toCompose()
