package ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo.model

import dev.icerock.moko.resources.desc.image.ImageDesc
import dev.icerock.moko.resources.desc.image.Url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.tetraquark.ton.explorer.core.ton.TonLiteClientApi
import ru.tetraquark.ton.explorer.core.ton.entity.JettonContract
import ru.tetraquark.ton.explorer.core.ton.entity.JettonWalletContract
import ru.tetraquark.ton.explorer.core.ton.entity.NftCollectionContract
import ru.tetraquark.ton.explorer.core.ton.entity.NftDnsCollectionContract
import ru.tetraquark.ton.explorer.core.ton.entity.NftDnsItemContract
import ru.tetraquark.ton.explorer.core.ton.entity.NftItemContract
import ru.tetraquark.ton.explorer.core.ton.entity.TonAddress
import ru.tetraquark.ton.explorer.core.ton.entity.UnknownContract
import ru.tetraquark.ton.explorer.core.ton.entity.WalletContract
import ru.tetraquark.ton.explorer.core.ton.entity.image.ImageData
import ru.tetraquark.ton.explorer.core.ton.entity.image.Imageable
import ru.tetraquark.ton.explorer.core.ton.utils.handleMetadataUri
import ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo.model.entity.AddressInfo
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.CoinAmount
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.ContractState
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.ToncoinAmount
import ru.tetraquark.ton.explorer.features.component.blockchain.mapAddress
import ru.tetraquark.ton.explorer.lib.resources.imagedesc.Base64
import ru.tetraquark.ton.explorer.lib.resources.imagedesc.Binary
import ru.tetraquark.ton.explorer.core.ton.entity.ContractInfo as TonContractInfo
import ru.tetraquark.ton.explorer.core.ton.entity.ContractState as TonContractState

class AddressInfoRepository(
    private val tonLiteClientApi: TonLiteClientApi
) {
    suspend fun getAddressInfo(address: String): AddressInfo? = withContext(Dispatchers.IO) {
        tonLiteClientApi.loadAccountInfo(TonAddress.BasicMasterchain(address))
            ?.mapToAddressInfo()
    }

    private fun TonContractState.mapState(): ContractState {
        return when (this) {
            TonContractState.INACTIVE -> ContractState.INACTIVE
            TonContractState.ACTIVE -> ContractState.ACTIVE
            TonContractState.FROZEN -> ContractState.FROZEN
        }
    }

    private fun TonContractInfo.mapToAddressInfo(): AddressInfo {
        return when (this) {
            is JettonContract -> AddressInfo.JettonContract(
                address = this.address.mapAddress(),
                balance = CoinAmount.ToncoinAmount(this.balance),
                state = this.state.mapState(),
                isMintable = this.isMintable,
                totalSupply = this.totalSupply,
                adminAddress = this.address.mapAddress(),
                name = this.metadata.name.orUnknownText(),
                descriptionText = this.metadata.description,
                symbol = this.metadata.symbol.orUnknownText(),
                logoImage = this.metadata.getImage()
            )
            is JettonWalletContract -> AddressInfo.JettonWalletContract(
                address = this.address.mapAddress(),
                balance = CoinAmount.ToncoinAmount(this.balance),
                state = this.state.mapState(),
                ownerAddress = this.ownerAddress.mapAddress(),
                rootAddress = this.rootAddress.mapAddress(),
                walletBalance = CoinAmount(
                    amount = this.walletBalance,
                    decimals = this.metadata.decimals ?: -1,
                    symbol = this.metadata.symbol.orUnknownText()
                ),
                logoImage = this.metadata.getImage()
            )
            is NftCollectionContract -> AddressInfo.NftCollectionContract(
                address = this.address.mapAddress(),
                balance = CoinAmount.ToncoinAmount(this.balance),
                state = this.state.mapState(),
                ownerAddress = this.ownerAddress.mapAddress(),
                nftItemsCount = this.nftItemsCount,
                name = this.metadata.name.orUnknownText(),
                descriptionText = this.metadata.description,
                image = this.metadata.getImage(),
                marketplace = this.metadata.marketplace
            )
            is NftDnsCollectionContract -> AddressInfo.NftDnsCollectionContract(
                address = this.address.mapAddress(),
                balance = CoinAmount.ToncoinAmount(this.balance),
                state = this.state.mapState(),
                name = this.metadata.name.orUnknownText(),
                descriptionText = this.metadata.description,
                image = this.metadata.getImage(),
            )
            is NftItemContract -> AddressInfo.NftItemContract(
                address = this.address.mapAddress(),
                balance = CoinAmount.ToncoinAmount(this.balance),
                state = this.state.mapState(),
                init = this.init,
                index = this.index,
                collectionAddress = this.collectionAddress.mapAddress(),
                ownerAddress = this.ownerAddress.mapAddress(),
                name = this.metadata.name.orUnknownText(),
                descriptionText = this.metadata.description,
                image = this.metadata.getImage(),
                contentUrl = this.metadata.contentUrl
            )
            is NftDnsItemContract -> AddressInfo.NftDnsItemContract(
                address = this.address.mapAddress(),
                balance = CoinAmount.ToncoinAmount(this.balance),
                state = this.state.mapState(),
                init = this.init,
                collectionAddress = this.collectionAddress.mapAddress(),
                ownerAddress = this.ownerAddress.mapAddress(),
                dnsName = this.dnsName
            )
            is WalletContract -> AddressInfo.WalletContract(
                address = this.address.mapAddress(),
                balance = CoinAmount.ToncoinAmount(this.balance),
                state = this.state.mapState(),
                versionName = this.walletVersion.name
            )
            is UnknownContract -> AddressInfo.UnknownContract(
                address = this.address.mapAddress(),
                balance = CoinAmount.ToncoinAmount(this.balance),
                state = this.state.mapState(),
            )
        }
    }

    private fun Imageable?.getImage(): ImageDesc? {
        if (this == null || (this.imageData == null && this.imageUrl == null)) return null
        return imageUrl?.let {
            ImageDesc.Url(handleMetadataUri(it))
        } ?: imageData?.let {
            when (it) {
                is ImageData.Base64 -> ImageDesc.Base64(it.data)
                is ImageData.Binary -> ImageDesc.Binary(it.data)
            }
        }
    }

    private fun String?.orUnknownText(): String = this ?: DASH_SYMBOLS

    companion object {
        private const val DASH_SYMBOLS = "---"
    }
}
