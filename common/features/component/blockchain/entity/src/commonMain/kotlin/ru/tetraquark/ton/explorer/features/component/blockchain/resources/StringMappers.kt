package ru.tetraquark.ton.explorer.features.component.blockchain.resources

import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.ContractState
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.Address
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.MRBlockchain

fun ContractState.toStringDesc(): StringDesc {
    return when (this) {
        ContractState.INACTIVE -> MRBlockchain.strings.contract_state_inactive.desc()
        ContractState.ACTIVE -> MRBlockchain.strings.contract_state_active.desc()
        ContractState.FROZEN -> MRBlockchain.strings.contract_state_frozen.desc()
    }
}

fun Address.toStringDesc(): StringDesc = when (this) {
    is Address.SimpleAddress -> address.desc()
    is Address.ExternalAddress -> base64Address.desc()
    is Address.NoAddress -> MRBlockchain.strings.noaddress.desc()
}
