package ru.tetraquark.ton.explorer.core.ton.entity

import com.soywiz.kbignum.BigNum

class Transaction(
    val timestamp: Long,
    val ltInfo: LtInfo,
    val prevLtInfo: LtInfo,
    val totalFee: BigNum,
    val inMessageInfo: MessageInfo?,
    val outMessagesInfo: List<MessageInfo>,
) {
    override fun toString(): String {
        return "[$timestamp] lt: ${ltInfo.lt}"
    }
}
