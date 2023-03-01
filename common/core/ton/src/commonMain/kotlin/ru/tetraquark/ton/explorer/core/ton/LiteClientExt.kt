package ru.tetraquark.ton.explorer.core.ton

import kotlinx.coroutines.delay
import org.ton.block.Transaction
import org.ton.boc.BagOfCells
import org.ton.lite.api.exception.LiteServerUnknownException
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerTransactionList
import org.ton.lite.api.liteserver.functions.LiteServerGetTransactions
import org.ton.lite.client.LiteClient
import org.ton.tlb.loadTlb

internal suspend fun LiteClient.loadTransactions(
    count: Int,
    accountId: LiteServerAccountId,
    lt: Long,
    hash: ByteArray
): List<Transaction> {
    require(count > 0) { "Count must be greater than 0" }
    val resultList = mutableListOf<Transaction>()
    var requestResultList: List<Transaction>
    var fromLt = lt
    var fromHash = hash
    do {
        var requestResult: LiteServerTransactionList? = null
        do {
            try {
                requestResult = liteApi(
                    LiteServerGetTransactions(
                        count = count - resultList.size,
                        account = accountId,
                        lt = fromLt,
                        hash = fromHash
                    )
                )
            } catch (lsEx: LiteServerUnknownException) {
                delay(500)
            }
        } while (requestResult == null)

        if (requestResult.ids.isEmpty()) return resultList

        requestResultList = BagOfCells(requestResult.transactions).roots
            .map { cell -> cell.beginParse().run { loadTlb(Transaction) } }
            .also(resultList::addAll)

        requestResultList.last().let {
            if (it.prevTransLt.toLong() == 0L) {
                return resultList
            } else {
                fromLt = it.prevTransLt.toLong()
                fromHash = it.prevTransHash.toByteArray()
                delay(300)
            }
        }
    } while (requestResultList.isNotEmpty() || resultList.size < count)
    return resultList
}
