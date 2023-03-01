package ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo.model.entity

import dev.icerock.moko.resources.desc.image.ImageDesc
import com.soywiz.kbignum.BigNum
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.Address
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.CoinAmount
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.ContractState

sealed class AddressInfo {
    abstract val address: Address
    abstract val state: ContractState
    abstract val balance: CoinAmount

    class UnknownContract(
        override val address: Address,
        override val balance: CoinAmount,
        override val state: ContractState
    ) : AddressInfo()

    class WalletContract(
        override val address: Address,
        override val balance: CoinAmount,
        override val state: ContractState,
        val versionName: String
    ) : AddressInfo()

    class NftCollectionContract(
        override val address: Address,
        override val balance: CoinAmount,
        override val state: ContractState,
        val ownerAddress: Address,
        val nftItemsCount: Long,
        val name: String,
        val descriptionText: String?,
        val image: ImageDesc?,
        val marketplace: String?,
    ) : AddressInfo()

    class NftDnsCollectionContract(
        override val address: Address,
        override val balance: CoinAmount,
        override val state: ContractState,
        val name: String,
        val descriptionText: String?,
        val image: ImageDesc?,
    ) : AddressInfo()

    class NftItemContract(
        override val address: Address,
        override val balance: CoinAmount,
        override val state: ContractState,
        val init: Boolean,
        val index: Long,
        val collectionAddress: Address,
        val ownerAddress: Address?,
        val name: String,
        val descriptionText: String?,
        val image: ImageDesc?,
        val contentUrl: String?,
    ) : AddressInfo()

    class NftDnsItemContract(
        override val address: Address,
        override val balance: CoinAmount,
        override val state: ContractState,
        val init: Boolean,
        val collectionAddress: Address,
        val ownerAddress: Address?,
        val dnsName: String,
    ) : AddressInfo()

    class JettonContract(
        override val address: Address,
        override val balance: CoinAmount,
        override val state: ContractState,
        val isMintable: Boolean,
        val totalSupply: BigNum,
        val adminAddress: Address,
        val name: String,
        val descriptionText: String? = null,
        val symbol: String,
        val logoImage: ImageDesc?
    ) : AddressInfo() {
        val totalSupplyText: String
            get() = totalSupply.toString()
    }

    class JettonWalletContract(
        override val address: Address,
        override val balance: CoinAmount,
        override val state: ContractState,
        val ownerAddress: Address,
        val rootAddress: Address,
        val walletBalance: CoinAmount,
        val logoImage: ImageDesc?
    ) : AddressInfo()
}
