package ru.tetraquark.ton.explorer.lib.resources.imagedesc

import dev.icerock.moko.resources.desc.image.ImageDesc

class ImageDescBase64(val data: String) : ImageDesc

@Suppress("FunctionName")
fun ImageDesc.Companion.Base64(base64: String): ImageDesc = ImageDescBase64(base64)
