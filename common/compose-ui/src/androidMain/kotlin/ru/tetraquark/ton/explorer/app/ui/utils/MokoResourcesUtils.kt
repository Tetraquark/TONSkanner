package ru.tetraquark.ton.explorer.app.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.desc.StringDesc

@Composable
actual fun StringDesc.asString(): String = toString(LocalContext.current)

@Composable
actual fun ColorResource.getLightColor(): Color = Color(getColor(LocalContext.current))

@Composable
actual fun ColorResource.getDarkColor(): Color = Color(getColor(LocalContext.current))
