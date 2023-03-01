package ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo

import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.resources.format
import ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo.model.entity.AddressInfo

fun AddressInfo.getTypeText(): StringDesc {
    return when (this) {
        is AddressInfo.JettonContract -> MRAddressInfo.strings.contract_type_jetton.desc()
        is AddressInfo.JettonWalletContract ->
            MRAddressInfo.strings.contract_type_jetton_wallet.desc()
        is AddressInfo.NftCollectionContract -> MRAddressInfo.strings.contract_type_nft_collection.desc()
        is AddressInfo.NftItemContract -> MRAddressInfo.strings.contract_type_nft_item.desc()
        is AddressInfo.UnknownContract -> MRAddressInfo.strings.contract_type_unknown.desc()
        is AddressInfo.WalletContract -> MRAddressInfo.strings.contract_type_wallet.format(
            listOf(versionName)
        )
        is AddressInfo.NftDnsCollectionContract ->
            MRAddressInfo.strings.contract_type_nft_dns_collection.desc()
        is AddressInfo.NftDnsItemContract -> MRAddressInfo.strings.contract_type_nft_dns_item.desc()
    }
}
