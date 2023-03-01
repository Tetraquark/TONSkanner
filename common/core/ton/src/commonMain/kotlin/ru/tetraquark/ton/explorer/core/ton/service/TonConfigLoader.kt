package ru.tetraquark.ton.explorer.core.ton.service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import ru.tetraquark.ton.explorer.core.ton.TonLiteClient
import ru.tetraquark.ton.explorer.core.ton.entity.TonConfig

class TonConfigLoader(
    val tonConfig: TonConfig,
    private val json: Json,
    private val httpClient: HttpClient
) : TonLiteClient.TonConfigLoader {
    override suspend fun loadLiteClientConfig(): LiteClientConfigGlobal {
        return when (tonConfig) {
            is TonConfig.Text -> json.decodeFromString(tonConfig.text)
            is TonConfig.Url -> {
                val textConfig = httpClient.get(
                    urlString = tonConfig.url
                ).bodyAsText()
                json.decodeFromString(textConfig)
            }
        }
    }
}
