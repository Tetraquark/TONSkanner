package ru.tetraquark.ton.explorer.core.ton.entity

sealed interface TonAddress {
    data class BasicMasterchain(val userFriendly: String) : TonAddress
    data class Other(val base64Address: String) : TonAddress
    object None : TonAddress
}
