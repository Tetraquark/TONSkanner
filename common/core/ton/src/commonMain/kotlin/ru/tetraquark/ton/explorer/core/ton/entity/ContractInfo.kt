package ru.tetraquark.ton.explorer.core.ton.entity

import com.soywiz.kbignum.BigNum
import ru.tetraquark.ton.explorer.core.ton.entity.metadata.JettonMetadata
import ru.tetraquark.ton.explorer.core.ton.entity.metadata.NftCollectionMetadata
import ru.tetraquark.ton.explorer.core.ton.entity.metadata.NftItemMetadata

sealed interface ContractInfo {
    val address: TonAddress.BasicMasterchain
    val balance: BigNum
    val state: ContractState
}

class UnknownContract(
    override val address: TonAddress.BasicMasterchain,
    override val balance: BigNum,
    override val state: ContractState,
) : ContractInfo

class WalletContract(
    override val address: TonAddress.BasicMasterchain,
    override val balance: BigNum,
    override val state: ContractState,
    val walletVersion: WalletVersion
) : ContractInfo

class NftCollectionContract(
    override val address: TonAddress.BasicMasterchain,
    override val balance: BigNum,
    override val state: ContractState,
    val ownerAddress: TonAddress,
    val nftItemsCount: Long,
    val metadata: NftCollectionMetadata,
) : ContractInfo

class NftDnsCollectionContract(
    override val address: TonAddress.BasicMasterchain,
    override val balance: BigNum,
    override val state: ContractState,
    val metadata: NftCollectionMetadata,
) : ContractInfo

class NftItemContract(
    override val address: TonAddress.BasicMasterchain,
    override val balance: BigNum,
    override val state: ContractState,
    val init: Boolean,
    val index: Long,
    val collectionAddress: TonAddress,
    val ownerAddress: TonAddress,
    val metadata: NftItemMetadata,
) : ContractInfo

class NftDnsItemContract(
    override val address: TonAddress.BasicMasterchain,
    override val balance: BigNum,
    override val state: ContractState,
    val init: Boolean,
    val collectionAddress: TonAddress,
    val ownerAddress: TonAddress,
    val dnsName: String
) : ContractInfo

class JettonContract(
    override val address: TonAddress.BasicMasterchain,
    override val balance: BigNum,
    override val state: ContractState,
    val isMintable: Boolean,
    val totalSupply: BigNum,
    val adminAddress: TonAddress,
    val metadata: JettonMetadata
) : ContractInfo

class JettonWalletContract(
    override val address: TonAddress.BasicMasterchain,
    override val balance: BigNum,
    override val state: ContractState,
    val ownerAddress: TonAddress,
    val rootAddress: TonAddress,
    val metadata: JettonMetadata,
    val walletBalance: BigNum
) : ContractInfo
