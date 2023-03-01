package ru.tetraquark.ton.explorer.core.ton.entity

import com.soywiz.kbignum.BigNum

sealed interface MessageInfo {
    val fromAddress: TonAddress
    val toAddress: TonAddress
    val textComment: String?

    class ExtInMsgInfo(
        override val fromAddress: TonAddress,
        override val toAddress: TonAddress,
        override val textComment: String?,
        val importFee: BigNum
    ) : MessageInfo

    class ExtOutMsgInfo(
        override val fromAddress: TonAddress,
        override val toAddress: TonAddress,
        override val textComment: String?,
        val createdLt: Long
    ) : MessageInfo

    class IntMsgInfo(
        override val fromAddress: TonAddress,
        override val toAddress: TonAddress,
        override val textComment: String?,
        val fwdFee: BigNum,
        val ihrFee: BigNum,
        val tonAmount: BigNum,
        val bounce: Boolean,
        val bounced: Boolean,
        val createdLt: Long
    ) : MessageInfo
}
