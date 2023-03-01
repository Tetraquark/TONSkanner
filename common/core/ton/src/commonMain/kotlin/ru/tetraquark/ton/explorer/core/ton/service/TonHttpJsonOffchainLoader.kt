package ru.tetraquark.ton.explorer.core.ton.service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import ru.tetraquark.ton.explorer.core.ton.TonLiteClient
import ru.tetraquark.ton.explorer.core.ton.utils.handleMetadataUri

class TonHttpJsonOffchainLoader(
    private val json: Json,
    private val httpClient: HttpClient,
    private val urlHandler: (String) -> String = ::handleMetadataUri
) : TonLiteClient.OffchainDataLoader {
    override suspend fun <T> getOffchainData(
        url: String,
        deserializer: DeserializationStrategy<T>
    ): T {
        val handledUrl = urlHandler(url)
        return httpClient.get(handledUrl)
            .bodyAsText()
            .let { json.decodeFromString(deserializer, it) }
    }
}
