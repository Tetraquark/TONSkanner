package ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model.entity

import ru.tetraquark.ton.explorer.features.component.blockchain.entity.Address

class MessageAddress(
    val address: Address,
    private val targetAddress: Address
) {
    val isTargetAddress: Boolean
        get() = address == targetAddress
    val isNoAddress: Boolean
        get() = address is Address.NoAddress
}
