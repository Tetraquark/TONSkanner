package ru.tetraquark.ton.explorer.core.ton.entity.image

import kotlinx.serialization.Serializable

@Serializable(with = ImageDataJsonSerializer::class)
sealed interface ImageData {
    class Base64(val data: String) : ImageData
    class Binary(val data: ByteArray) : ImageData
}
