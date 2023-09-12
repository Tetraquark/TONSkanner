package ru.tetraquark.ton.explorer.app.ui.utils

import dev.icerock.moko.resources.desc.image.ImageDesc

actual class ImageDescDrawable(val resourceId: Int) : ImageDesc

@Suppress("FunctionName")
fun ImageDesc.Companion.Drawable(resourceId: Int): ImageDesc = ImageDescDrawable(resourceId)
