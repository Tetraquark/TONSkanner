package ru.tetraquark.ton.explorer.app.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc

@Composable
expect fun StringDesc.asString(): String

@Composable
fun StringResource.asString(): String = desc().asString()

@Composable
expect fun ColorResource.getLightColor(): Color

@Composable
expect fun ColorResource.getDarkColor(): Color

internal fun dev.icerock.moko.graphics.Color.toCompose(): Color = Color(red, green, blue, alpha)
