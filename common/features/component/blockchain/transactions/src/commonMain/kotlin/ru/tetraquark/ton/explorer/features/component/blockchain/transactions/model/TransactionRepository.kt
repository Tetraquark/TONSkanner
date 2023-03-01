package ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.tetraquark.ton.explorer.core.ton.TonLiteClientApi
import ru.tetraquark.ton.explorer.core.ton.entity.LtInfo
import ru.tetraquark.ton.explorer.core.ton.entity.MessageInfo
import ru.tetraquark.ton.explorer.core.ton.entity.TonAddress
import ru.tetraquark.ton.explorer.core.ton.entity.Transaction
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.Address
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.CoinAmount
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.ToncoinAmount
import ru.tetraquark.ton.explorer.features.component.blockchain.mapAddress
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model.entity.MessageAddress
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model.entity.TransactionListItem
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model.entity.TransactionMessage

class TransactionRepository(
    private val tonLiteClientApi: TonLiteClientApi
) {
    suspend fun loadTransactions(
        address: String,
        size: Int,
        from: Pair<Long, ByteArray>? = null,
    ): List<TransactionListItem> {
        val targetAddress = TonAddress.BasicMasterchain(address)
        return withContext(Dispatchers.IO) {
            tonLiteClientApi.loadTransactions(
                count = size,
                address = targetAddress,
                fromLtInfo = from?.let { LtInfo(it.first, it.second) }
            ).map { tr ->
                tr.toTransactionItem(targetAddress.mapAddress())
            }
        }
    }

    private fun Transaction.toTransactionItem(
        targetAddress: Address
    ): TransactionListItem {
        val id = (this.timestamp.hashCode() + this.ltInfo.lt.hashCode()).toLong()
        return TransactionListItem(
            id = id,
            timestamp = this.timestamp,
            totalFee = CoinAmount.ToncoinAmount(this.totalFee),
            inMessage = this.inMessageInfo.toTransactionMessage(targetAddress),
            outMessages = this.outMessagesInfo.mapNotNull {
                it.toTransactionMessage(targetAddress)
            },
            ltInfo = this.ltInfo.toPair(),
            prevLtInfo = this.prevLtInfo.toPair()
        )
    }

    private fun MessageInfo?.toTransactionMessage(
        targetAddress: Address
    ): TransactionMessage? {
        return when (this) {
            is MessageInfo.ExtInMsgInfo -> TransactionMessage.ExtInMsgInfo(
                fromAddress = MessageAddress(fromAddress.mapAddress(), targetAddress),
                toAddress = MessageAddress(toAddress.mapAddress(), targetAddress),
                textComment = this.textComment,
                importFee = CoinAmount.ToncoinAmount(this.importFee)
            )
            is MessageInfo.ExtOutMsgInfo -> TransactionMessage.ExtOutMsgInfo(
                fromAddress = MessageAddress(fromAddress.mapAddress(), targetAddress),
                toAddress = MessageAddress(toAddress.mapAddress(), targetAddress),
                textComment = this.textComment,
                createdLt = this.createdLt
            )
            is MessageInfo.IntMsgInfo -> TransactionMessage.IntMsgInfo(
                fromAddress = MessageAddress(fromAddress.mapAddress(), targetAddress),
                toAddress = MessageAddress(toAddress.mapAddress(), targetAddress),
                textComment = this.textComment,
                fwdFee = CoinAmount.ToncoinAmount(this.fwdFee),
                ihrFee = CoinAmount.ToncoinAmount(this.ihrFee),
                tonAmount = CoinAmount.ToncoinAmount(this.tonAmount),
                bounce = this.bounce,
                bounced = this.bounced,
                createdLt = this.createdLt
            )
            else -> null
        }
    }

    private fun LtInfo.toPair(): Pair<Long, ByteArray> = lt to hash
}
