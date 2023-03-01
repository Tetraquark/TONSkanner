package ru.tetraquark.ton.explorer.lib.resources.imagedesc

import dev.icerock.moko.resources.desc.image.ImageDesc

class ImageDescBinary(val data: ByteArray) : ImageDesc

@Suppress("FunctionName")
fun ImageDesc.Companion.Binary(bytes: ByteArray): ImageDesc = ImageDescBinary(bytes)
