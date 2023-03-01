package ru.tetraquark.ton.explorer.core.ton.entity.image

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializer(forClass = ImageData::class)
object ImageDataJsonSerializer : KSerializer<ImageData> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ImageData", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ImageData {
        val base64String = decoder.decodeString()
        return ImageData.Base64(base64String)
    }

    override fun serialize(encoder: Encoder, value: ImageData) {
        when (value) {
            is ImageData.Base64 -> encoder.encodeString(value.data)
            is ImageData.Binary -> error("JSON doesn't support binary format.")
        }
    }
}
