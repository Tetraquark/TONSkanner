package ru.tetraquark.ton.explorer.core.ton.exception

open class TonLiteClientException(
    override val cause: Throwable? = null,
    override val message: String? = null
) : Exception()
