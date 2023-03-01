package ru.tetraquark.ton.explorer.app.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import dev.icerock.moko.resources.desc.image.ImageDesc

@Composable
expect fun ImageDesc.toPainter(): Painter
