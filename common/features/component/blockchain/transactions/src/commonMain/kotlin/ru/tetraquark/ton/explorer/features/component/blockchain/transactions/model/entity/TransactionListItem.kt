package ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model.entity

import com.soywiz.klock.DateTime
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.CoinAmount

data class TransactionListItem(
    val id: Long,
    val timestamp: Long,
    val totalFee: CoinAmount,
    val inMessage: TransactionMessage?,
    val outMessages: List<TransactionMessage>,
    internal val ltInfo: Pair<Long, ByteArray>,
    internal val prevLtInfo: Pair<Long, ByteArray>,
) {
    fun formatDate(): String = DateTime.fromUnix(timestamp).local.format("dd-MM-yyyy")
}

sealed class TransactionMessage {
    abstract val fromAddress: MessageAddress
    abstract val toAddress: MessageAddress
    abstract val textComment: String?

    class ExtInMsgInfo(
        override val fromAddress: MessageAddress,
        override val toAddress: MessageAddress,
        override val textComment: String?,
        val importFee: CoinAmount
    ) : TransactionMessage()

    class ExtOutMsgInfo(
        override val fromAddress: MessageAddress,
        override val toAddress: MessageAddress,
        override val textComment: String?,
        val createdLt: Long
    ) : TransactionMessage()

    class IntMsgInfo(
        override val fromAddress: MessageAddress,
        override val toAddress: MessageAddress,
        override val textComment: String?,
        val fwdFee: CoinAmount,
        val ihrFee: CoinAmount,
        val tonAmount: CoinAmount,
        val bounce: Boolean,
        val bounced: Boolean,
        val createdLt: Long
    ) : TransactionMessage()
}
