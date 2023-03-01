package ru.tetraquark.ton.explorer.app.ui.utils

import dev.icerock.moko.resources.desc.image.ImageDesc

actual class ImageDescDrawable(val filePath: String) : ImageDesc

@Suppress("FunctionName")
fun ImageDesc.Companion.Drawable(filePath: String): ImageDesc = ImageDescDrawable(filePath)
