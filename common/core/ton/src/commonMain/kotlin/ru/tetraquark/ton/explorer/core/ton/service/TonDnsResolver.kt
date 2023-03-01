package ru.tetraquark.ton.explorer.core.ton.service

import ru.tetraquark.ton.explorer.core.ton.TonLiteClient
import ru.tetraquark.ton.explorer.core.ton.entity.TonAddress

class TonDnsResolver(
    private val tonDnsRootCollectionAddress: TonAddress.BasicMasterchain
) : TonLiteClient.TonDnsResolver {
    override suspend fun isRootDnsCollectionAddress(address: TonAddress): Boolean {
        return address == tonDnsRootCollectionAddress
    }
}
