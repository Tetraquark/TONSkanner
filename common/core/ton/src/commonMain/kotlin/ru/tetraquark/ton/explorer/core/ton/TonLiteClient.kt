package ru.tetraquark.ton.explorer.core.ton

import com.soywiz.kbignum.BigNum
import io.ktor.utils.io.printStack
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.DeserializationStrategy
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import org.ton.bigint.BigInt
import org.ton.block.AccountActive
import org.ton.block.AccountInfo
import org.ton.block.AccountNone
import org.ton.block.AddrStd
import org.ton.block.Transaction
import org.ton.block.VmCellSlice
import org.ton.block.VmStackCell
import org.ton.block.VmStackNumber
import org.ton.crypto.base64
import org.ton.lite.api.exception.LiteServerException
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.functions.LiteServerGetAccountState
import org.ton.lite.client.LiteClient
import ru.tetraquark.ton.explorer.core.ton.entity.ContractInfo
import ru.tetraquark.ton.explorer.core.ton.entity.ContractState
import ru.tetraquark.ton.explorer.core.ton.entity.JettonContract
import ru.tetraquark.ton.explorer.core.ton.entity.JettonWalletContract
import ru.tetraquark.ton.explorer.core.ton.entity.LtInfo
import ru.tetraquark.ton.explorer.core.ton.entity.NftCollectionContract
import ru.tetraquark.ton.explorer.core.ton.entity.NftDnsCollectionContract
import ru.tetraquark.ton.explorer.core.ton.entity.NftDnsItemContract
import ru.tetraquark.ton.explorer.core.ton.entity.NftItemContract
import ru.tetraquark.ton.explorer.core.ton.entity.TonAddress
import ru.tetraquark.ton.explorer.core.ton.entity.UnknownContract
import ru.tetraquark.ton.explorer.core.ton.entity.WalletContract
import ru.tetraquark.ton.explorer.core.ton.entity.WalletVersion
import ru.tetraquark.ton.explorer.core.ton.entity.metadata.JettonMetadata
import ru.tetraquark.ton.explorer.core.ton.entity.metadata.MetadataContent
import ru.tetraquark.ton.explorer.core.ton.entity.metadata.NftCollectionMetadata
import ru.tetraquark.ton.explorer.core.ton.entity.metadata.NftItemMetadata
import ru.tetraquark.ton.explorer.core.ton.exception.IncorrectUriException
import ru.tetraquark.ton.explorer.core.ton.exception.TonIncorrectAddressException
import ru.tetraquark.ton.explorer.core.ton.exception.TonRepositoryInitException
import ru.tetraquark.ton.explorer.core.ton.exception.TonUnexpectedResponseException
import ru.tetraquark.ton.explorer.core.ton.utils.LiteServerAccountId
import ru.tetraquark.ton.explorer.core.ton.utils.concatRefs
import ru.tetraquark.ton.explorer.core.ton.utils.loadMsgAddress
import ru.tetraquark.ton.explorer.core.ton.utils.mapOnChainJettonMetadata
import ru.tetraquark.ton.explorer.core.ton.utils.mapOnChainToNftCollectionMetadata
import ru.tetraquark.ton.explorer.core.ton.utils.mapOnChainToNftItemMetadata
import ru.tetraquark.ton.explorer.core.ton.utils.parseMetadataContent
import ru.tetraquark.ton.explorer.core.ton.utils.parseShardAccount

class TonLiteClient(
    private val configLoader: TonConfigLoader,
    private val offchainDataLoader: OffchainDataLoader,
    private val tonDnsResolver: TonDnsResolver,
) : TonLiteClientApi {
    private var liteClient: LiteClient? = null

    override val isInitialized: Boolean
        get() = liteClient != null

    override suspend fun init() {
        if (liteClient != null) error("Repository has already initialized")

        try {
            val config = configLoader.loadLiteClientConfig()
            liteClient = LiteClient(
                coroutineContext = currentCoroutineContext(),
                liteClientConfigGlobal = config
            ).apply {
                getServerTime()
            }
        } catch (ex: Exception) {
            throw TonRepositoryInitException(ex)
        }
    }

    fun close() {
        liteClient?.close()
        liteClient = null
    }

    override suspend fun loadTransactions(
        count: Int,
        address: TonAddress.BasicMasterchain,
        fromLtInfo: LtInfo?
    ): List<ru.tetraquark.ton.explorer.core.ton.entity.Transaction> {
        if (!isInitialized) init()
        val lc = liteClient ?: error("LiteClient not initialized")
        val accountId = address.toAccountId()

        val responseList = if (fromLtInfo != null) {
            runWithAttempts {
                lc.loadTransactions(
                    count = count,
                    accountId = accountId,
                    lt = fromLtInfo.lt,
                    hash = fromLtInfo.hash
                ).drop(1)
            }
        } else {
            val lastBlock = runWithAttempts { lc.getLastBlockId() }
            val accountState = try {
                runWithAttempts {
                    lc.liteApi(
                        LiteServerGetAccountState(
                            id = lastBlock,
                            account = accountId
                        )
                    )
                }
            } catch (illegalArgEx: IllegalArgumentException) {
                throw TonIncorrectAddressException(illegalArgEx)
            }

            val shardAccount = accountState.parseShardAccount()
                ?: throw TonUnexpectedResponseException()

            lc.loadTransactions(
                count = count,
                accountId = accountId,
                lt = shardAccount.lastTransLt.toLong(),
                hash = shardAccount.lastTransHash.toByteArray()
            )
        }

        return responseList.map { it.mapTransaction() }
    }

    override suspend fun loadAccountInfo(address: TonAddress.BasicMasterchain): ContractInfo? {
        if (!isInitialized) init()
        val lc = liteClient ?: error("LiteClient not initialized")
        val account = try {
            runWithAttempts { lc.getAccount(address.userFriendly) }
                ?: throw TonUnexpectedResponseException()
        } catch (illegalArgEx: IllegalArgumentException) {
            throw TonIncorrectAddressException(illegalArgEx)
        }

        return when (account) {
            is AccountNone -> null
            is AccountInfo -> {
                val state = account.storage.state.mapToContractState()
                val balance = account.storage.balance.coins.amount.value
                val walletVersion = (account.storage.state as? AccountActive)
                    ?.run(::checkWalletVersion)

                return if (walletVersion != null && walletVersion != WalletVersion.UNKNOWN) {
                    WalletContract(
                        address = address,
                        balance = balance.toBigNum(),
                        state = state,
                        walletVersion = walletVersion
                    )
                } else {
                    supervisorScope {
                        listOf(
                            async {
                                runCatching {
                                    lc.tryNftCollection(
                                        address,
                                        state,
                                        balance
                                    )
                                }.onFailure {
                                    it.printStack()
                                }.getOrNull()
                            },
                            async {
                                runCatching { lc.tryNftItem(address, state, balance) }.onFailure {
                                    it.printStack()
                                }.getOrNull()
                            },
                            async {
                                runCatching {
                                    lc.tryJettonWallet(
                                        address,
                                        state,
                                        balance
                                    )
                                }.onFailure {
                                    it.printStack()
                                }.getOrNull()
                            },
                            async {
                                runCatching {
                                    lc.tryJettonContract(
                                        address,
                                        state,
                                        balance
                                    )
                                }.onFailure {
                                    it.printStack()
                                }.getOrNull()
                            }
                        ).awaitAll().firstOrNull { it != null }
                    } ?: UnknownContract(address, balance.toBigNum(), state)
                }
            }
        }
    }

    private suspend fun LiteClient.tryNftCollection(
        contractAddress: TonAddress.BasicMasterchain,
        contractState: ContractState,
        contractBalance: BigInt,
    ): ContractInfo {
        val result = runWithAttempts {
            runSmcMethod(
                address = contractAddress.toAccountId(),
                methodName = "get_collection_data"
            )
        }

        val metadataContent = (result[1] as VmStackCell).cell
            .parseMetadataContent(::mapOnChainToNftCollectionMetadata)
        val metadata = loadMetadata(metadataContent, NftCollectionMetadata.serializer())

        return if (tonDnsResolver.isRootDnsCollectionAddress(contractAddress)) {
            NftDnsCollectionContract(
                address = contractAddress,
                balance = contractBalance.toBigNum(),
                state = contractState,
                metadata = metadata,
            )
        } else {
            val itemsCount = (result[2] as VmStackNumber).toLong()
            val ownerAddress = (result[0] as VmCellSlice).toCellSlice().loadMsgAddress()
                .toTonAddress()

            NftCollectionContract(
                address = contractAddress,
                balance = contractBalance.toBigNum(),
                state = contractState,
                ownerAddress = ownerAddress,
                nftItemsCount = itemsCount,
                metadata = metadata
            )
        }
    }

    private suspend fun LiteClient.tryNftItem(
        contractAddress: TonAddress.BasicMasterchain,
        contractState: ContractState,
        contractBalance: BigInt,
    ): ContractInfo {
        val result = runWithAttempts {
            runSmcMethod(
                address = contractAddress.toAccountId(),
                methodName = "get_nft_data"
            )
        }

        val isInitialized = (result[4] as VmStackNumber).toInt().let { it != 0 }
        val ownerAddress = if (isInitialized) {
            (result[1] as VmCellSlice).toCellSlice().loadMsgAddress().toTonAddress()
        } else {
            TonAddress.None
        }
        val collectionAddress = (result[2] as VmCellSlice).toCellSlice().loadMsgAddress()
            .toTonAddress()

        return if (tonDnsResolver.isRootDnsCollectionAddress(collectionAddress)) {
            val slice = runWithAttempts {
                runSmcMethod(
                    address = contractAddress.toAccountId(),
                    methodName = "get_domain"
                ).first() as VmCellSlice
            }
            val dnsName = String(slice.cell.concatRefs())

            NftDnsItemContract(
                address = contractAddress,
                balance = contractBalance.toBigNum(),
                state = contractState,
                init = isInitialized,
                collectionAddress = collectionAddress,
                ownerAddress = ownerAddress,
                dnsName = dnsName
            )
        } else {
            val indexVmNumber = result[3] as VmStackNumber
            val metadataVmCell = result[0] as VmStackCell

            val metadataContent =
                metadataVmCell.cell.parseMetadataContent(::mapOnChainToNftItemMetadata)
            val metadata = when (metadataContent) {
                is MetadataContent.OffChain -> {
                    runCatching {
                        loadMetadata(
                            metadataContent = metadataContent,
                            serializer = NftItemMetadata.serializer()
                        )
                    }.fold(
                        onSuccess = { it },
                        onFailure = { ex ->
                            if (ex is IncorrectUriException) {
                                val cell = runWithAttempts {
                                    runSmcMethod(
                                        address = (collectionAddress as TonAddress.BasicMasterchain)
                                            .toAccountId(),
                                        methodName = "get_nft_content",
                                        params = listOf(indexVmNumber, metadataVmCell)
                                    ).first() as VmStackCell
                                }

                                loadMetadata(
                                    metadataContent = cell.cell
                                        .parseMetadataContent(::mapOnChainToNftItemMetadata),
                                    serializer = NftItemMetadata.serializer()
                                )
                            } else {
                                throw ex
                            }
                        }
                    )

                }
                is MetadataContent.OnChain<*> -> {
                    metadataContent.metadata as NftItemMetadata
                }
            }

            NftItemContract(
                address = contractAddress,
                balance = contractBalance.toBigNum(),
                state = contractState,
                init = isInitialized,
                index = indexVmNumber.toLong(),
                collectionAddress = collectionAddress,
                ownerAddress = ownerAddress,
                metadata = metadata,
            )
        }
    }

    private suspend fun LiteClient.tryJettonContract(
        contractAddress: TonAddress.BasicMasterchain,
        contractState: ContractState,
        contractBalance: BigInt,
    ): ContractInfo {
        val result = runWithAttempts {
            runSmcMethod(
                address = contractAddress.toAccountId(),
                methodName = "get_jetton_data"
            )
        }

        val totalSupply = (result[4] as VmStackNumber).toBigInt()
        val isMintable = (result[3] as VmStackNumber).toInt().let { it == -1 }
        val adminAddress = (result[2] as VmCellSlice).toCellSlice().loadMsgAddress().toTonAddress()
        val metadataContent =
            (result[1] as VmStackCell).cell.parseMetadataContent(::mapOnChainJettonMetadata)

        val metadata = loadMetadata(metadataContent, JettonMetadata.serializer())

        return JettonContract(
            address = contractAddress,
            balance = contractBalance.toBigNum(),
            state = contractState,
            isMintable = isMintable,
            totalSupply = totalSupply.toBigNum(),
            adminAddress = adminAddress,
            metadata = metadata
        )
    }

    private suspend fun LiteClient.tryJettonWallet(
        contractAddress: TonAddress.BasicMasterchain,
        contractState: ContractState,
        contractBalance: BigInt,
    ): ContractInfo {
        val result = runWithAttempts {
            runSmcMethod(
                address = contractAddress.toAccountId(),
                methodName = "get_wallet_data"
            )
        }

        val ownerAddress =
            (result[2] as? VmCellSlice)?.toCellSlice().loadMsgAddress().toTonAddress()
        val rootAddress = (result[1] as? VmCellSlice)?.toCellSlice().loadMsgAddress().toTonAddress()
        val balance = (result[3] as? VmStackNumber)?.toLong() ?: 0

        val jettonRootContract = tryJettonContract(
            contractAddress = rootAddress as TonAddress.BasicMasterchain,
            contractState = ContractState.ACTIVE,
            contractBalance = BigInt.ZERO
        ) as JettonContract

        return JettonWalletContract(
            address = contractAddress,
            balance = contractBalance.toBigNum(),
            state = contractState,
            ownerAddress = ownerAddress,
            rootAddress = rootAddress,
            metadata = jettonRootContract.metadata,
            walletBalance = BigNum(com.soywiz.kbignum.BigInt(balance), 0)
        )
    }

    private suspend fun <T> loadMetadata(
        metadataContent: MetadataContent,
        serializer: DeserializationStrategy<T>
    ): T {
        return when (metadataContent) {
            is MetadataContent.OffChain -> {
                offchainDataLoader.getOffchainData(
                    metadataContent.url,
                    serializer
                )
            }
            is MetadataContent.OnChain<*> -> metadataContent.metadata as T
        }
    }

    private fun checkWalletVersion(activeState: AccountActive): WalletVersion {
        return activeState.value.code.value?.value
            ?.concatRefs()
            ?.let(::base64)
            ?.let { WalletVersion.findByKey(it) }
            ?: WalletVersion.UNKNOWN
    }

    private suspend fun <T> runWithAttempts(
        attempts: Int = 2,
        action: suspend () -> T
    ): T {
        var lastException = Throwable()
        repeat(attempts) {
            runCatching { action() }.fold(
                onSuccess = { return it },
                onFailure = { ex ->
                    lastException = ex
                    if (ex !is LiteServerException) throw ex
                }
            )
        }
        throw lastException
    }

    private fun TonAddress.BasicMasterchain.toAccountId(): LiteServerAccountId {
        return LiteServerAccountId(AddrStd(this.userFriendly))
    }

    fun interface TonDnsResolver {
        suspend fun isRootDnsCollectionAddress(address: TonAddress): Boolean
    }

    fun interface TonConfigLoader {
        suspend fun loadLiteClientConfig(): LiteClientConfigGlobal
    }

    interface OffchainDataLoader {
        suspend fun <T> getOffchainData(url: String, deserializer: DeserializationStrategy<T>): T
    }
}
