package ru.tetraquark.ton.explorer.core.ton.entity.metadata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.tetraquark.ton.explorer.core.ton.entity.image.ImageData
import ru.tetraquark.ton.explorer.core.ton.entity.image.Imageable

@Serializable
class NftItemMetadata(
    val name: String? = null,
    val description: String? = null,
    @SerialName("image")
    override val imageUrl: String? = null,
    @SerialName("image_data")
    override val imageData: ImageData? = null,
    @SerialName("content_url")
    val contentUrl: String? = null
) : Imageable