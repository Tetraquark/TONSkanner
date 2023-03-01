package ru.tetraquark.ton.explorer.core.ton.utils

import org.ton.crypto.digest.sha256
import ru.tetraquark.ton.explorer.core.ton.entity.image.ImageData
import ru.tetraquark.ton.explorer.core.ton.entity.metadata.JettonMetadata
import ru.tetraquark.ton.explorer.core.ton.entity.metadata.NftCollectionMetadata
import ru.tetraquark.ton.explorer.core.ton.entity.metadata.NftItemMetadata
import ru.tetraquark.ton.explorer.core.ton.exception.IncorrectUriException

private const val HTTP_URL_PREFIX = "http://"
private const val HTTPS_URL_PREFIX = "https://"
private const val IPFS_URL_PREFIX = "ipfs://"
private const val CLOUDFLARE_IPFS_URL_PREFIX = "https://cloudflare-ipfs.com/ipfs/"

/**
 * Converts IPFS URI to Cloudflare gateway HTTP URI.
 */
fun handleMetadataUri(uri: String): String = when {
    uri.startsWith(HTTP_URL_PREFIX) || uri.startsWith(HTTPS_URL_PREFIX) -> uri
    uri.startsWith(IPFS_URL_PREFIX) -> CLOUDFLARE_IPFS_URL_PREFIX + uri.removePrefix(IPFS_URL_PREFIX)
    else -> throw IncorrectUriException(uri)
}

internal fun mapOnChainToNftCollectionMetadata(map: Map<String, Any?>): NftCollectionMetadata {
    return NftCollectionMetadata(
        name = map["name".asChunkKey()]?.castToString(),
        description = map["description".asChunkKey()]?.castToString(),
        imageUrl = map["image".asChunkKey()]?.castToString(),
        imageData = map["image_data".asChunkKey()]?.castToImageData(),
        marketplace = map["marketplace".asChunkKey()]?.castToString(),
        socialLinks = emptyList() // TODO?
    )
}

internal fun mapOnChainToNftItemMetadata(map: Map<String, Any?>): NftItemMetadata {
    return NftItemMetadata(
        name = map["name".asChunkKey()]?.castToString(),
        description = map["description".asChunkKey()]?.castToString(),
        imageUrl = map["image".asChunkKey()]?.castToString(),
        imageData = map["image_data".asChunkKey()]?.castToImageData(),
        contentUrl = map["content_url".asChunkKey()]?.castToString(),
    )
}

internal fun mapOnChainJettonMetadata(map: Map<String, Any?>): JettonMetadata {
    return JettonMetadata(
        name = map["name".asChunkKey()]?.castToString(),
        symbol = map["symbol".asChunkKey()]?.castToString(),
        description = map["description".asChunkKey()]?.castToString(),
        decimals = map["decimals".asChunkKey()]?.castToInt(),
        imageUrl = map["image".asChunkKey()]?.castToString(),
        imageData = map["image_data".asChunkKey()]?.castToImageData(),
    )
}

private fun String.asChunkKey(): String = String(sha256(this.toByteArray()))

private fun Any.castToString(): String = (this as ByteArray).run(::String)

private fun Any.castToInt(): Int {
    var result = 0
    (this as ByteArray).forEachIndexed { index, byte ->
        result = result or (byte.toInt() shl 8 * index)
    }
    return result
}

/**
 * Onchain image_data can be only binary.
 */
private fun Any.castToImageData(): ImageData = ImageData.Binary(this as ByteArray)
