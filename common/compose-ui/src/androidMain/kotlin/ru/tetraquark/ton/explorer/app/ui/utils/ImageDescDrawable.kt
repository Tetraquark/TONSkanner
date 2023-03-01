package ru.tetraquark.ton.explorer.app.ui.utils

import androidx.annotation.DrawableRes
import dev.icerock.moko.resources.desc.image.ImageDesc

actual class ImageDescDrawable(@DrawableRes val resourceId: Int) : ImageDesc

@Suppress("FunctionName")
fun ImageDesc.Companion.Drawable(resourceId: Int): ImageDesc = ImageDescDrawable(resourceId)
