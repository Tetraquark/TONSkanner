package ru.tetraquark.ton.explorer.features.component.blockchain

import ru.tetraquark.ton.explorer.core.ton.entity.TonAddress
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.Address

fun TonAddress.mapAddress(): Address {
    return when (this) {
        is TonAddress.BasicMasterchain -> Address.SimpleAddress(this.userFriendly)
        is TonAddress.None -> Address.NoAddress
        is TonAddress.Other -> Address.ExternalAddress(this.base64Address)
    }
}
