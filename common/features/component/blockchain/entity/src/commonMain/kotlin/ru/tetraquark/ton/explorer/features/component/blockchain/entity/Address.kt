package ru.tetraquark.ton.explorer.features.component.blockchain.entity

sealed interface Address {
    data class SimpleAddress(val address: String) : Address
    data class ExternalAddress(val base64Address: String) : Address
    object NoAddress : Address
}
