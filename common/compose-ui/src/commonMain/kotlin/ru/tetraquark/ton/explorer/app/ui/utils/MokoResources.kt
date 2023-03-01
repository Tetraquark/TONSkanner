package ru.tetraquark.ton.explorer.app.ui.utils

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc

@Composable
expect fun StringDesc.asString(): String

@Composable
fun StringResource.asString(): String = desc().asString()
