package ru.tetraquark.ton.explorer.core.ton.entity

sealed interface TonConfig {
    class Url(val url: String) : TonConfig
    class Text(val text: String) : TonConfig
}
