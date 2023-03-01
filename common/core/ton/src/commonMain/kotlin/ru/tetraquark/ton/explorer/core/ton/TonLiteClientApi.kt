package ru.tetraquark.ton.explorer.core.ton

import ru.tetraquark.ton.explorer.core.ton.entity.ContractInfo
import ru.tetraquark.ton.explorer.core.ton.entity.LtInfo
import ru.tetraquark.ton.explorer.core.ton.entity.TonAddress
import ru.tetraquark.ton.explorer.core.ton.entity.Transaction

interface TonLiteClientApi {
    val isInitialized: Boolean

    suspend fun init()

    suspend fun loadAccountInfo(address: TonAddress.BasicMasterchain): ContractInfo?

    suspend fun loadTransactions(
        count: Int,
        address: TonAddress.BasicMasterchain,
        fromLtInfo: LtInfo?
    ): List<Transaction>
}
