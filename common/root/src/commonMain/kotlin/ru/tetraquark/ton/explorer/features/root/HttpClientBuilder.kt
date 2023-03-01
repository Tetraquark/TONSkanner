package ru.tetraquark.ton.explorer.features.root

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import kotlinx.serialization.json.Json

internal fun buildKtorHttpClient(
    json: Json,
    httpClientEngine: HttpClientEngine?,
): HttpClient {
    val httpClientConfig: HttpClientConfig<*>.() -> Unit = {
        expectSuccess = false

        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    println("[KTOR-LOG]: $message")
                }
            }
        }
    }

    return if (httpClientEngine != null) {
        HttpClient(httpClientEngine, httpClientConfig)
    } else {
        HttpClient(httpClientConfig)
    }
}
