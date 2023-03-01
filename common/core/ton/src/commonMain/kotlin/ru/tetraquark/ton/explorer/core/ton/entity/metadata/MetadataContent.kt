package ru.tetraquark.ton.explorer.core.ton.entity.metadata

sealed interface MetadataContent {
    class OffChain(val url: String) : MetadataContent
    class OnChain<T>(val metadata: T) : MetadataContent
}
