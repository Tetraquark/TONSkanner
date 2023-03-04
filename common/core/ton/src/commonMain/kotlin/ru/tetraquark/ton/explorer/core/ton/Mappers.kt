package ru.tetraquark.ton.explorer.core.ton

import com.soywiz.kbignum.BigNum
import org.ton.bigint.BigInt
import org.ton.block.AccountActive
import org.ton.block.AccountFrozen
import org.ton.block.AccountState
import org.ton.block.AccountUninit
import org.ton.block.AddrExtern
import org.ton.block.AddrStd
import org.ton.block.AddrVar
import org.ton.block.ExtInMsgInfo
import org.ton.block.ExtOutMsgInfo
import org.ton.block.IntMsgInfo
import org.ton.block.Message
import org.ton.block.MsgAddress
import org.ton.block.Transaction
import org.ton.cell.Cell
import org.ton.crypto.base64
import ru.tetraquark.ton.explorer.core.ton.entity.ContractState
import ru.tetraquark.ton.explorer.core.ton.entity.LtInfo
import ru.tetraquark.ton.explorer.core.ton.entity.MessageInfo
import ru.tetraquark.ton.explorer.core.ton.entity.TonAddress
import ru.tetraquark.ton.explorer.core.ton.utils.parseComment

internal fun BigInt.toBigNum(): BigNum = BigNum(toString())

internal fun AccountState.mapToContractState(): ContractState = when (this) {
    is AccountActive -> ContractState.ACTIVE
    is AccountFrozen -> ContractState.FROZEN
    is AccountUninit -> ContractState.INACTIVE
}

internal fun MsgAddress?.toTonAddress(): TonAddress {
    return when (this) {
        is AddrStd -> TonAddress.BasicMasterchain(this.toString(userFriendly = true))
        is AddrExtern -> TonAddress.Other(base64(this.externalAddress.toByteArray()))
        is AddrVar -> TonAddress.Other(base64(this.address.toByteArray()))
        else -> TonAddress.None
    }
}

internal fun Transaction.mapTransaction(
    timestampMultiplier: Long = 1000L
): ru.tetraquark.ton.explorer.core.ton.entity.Transaction {
    val inMessageInfo = r1.value.inMsg.value?.value?.mapToMessageInfo()
    val outMessageInfoList = r1.value.outMsgs
        .map { it.second.value.mapToMessageInfo() }
        .toList()

    return ru.tetraquark.ton.explorer.core.ton.entity.Transaction(
        timestamp = this.now.toLong() * timestampMultiplier,
        ltInfo = LtInfo(
            lt = this.lt.toLong(),
            hash = this.hash()
        ),
        prevLtInfo = LtInfo(
            lt = this.prevTransLt.toLong(),
            hash = this.prevTransHash.toByteArray()
        ),
        totalFee = totalFees.coins.amount.value.toBigNum(),
        inMessageInfo = inMessageInfo,
        outMessagesInfo = outMessageInfoList
    )
}

private fun Message<Cell>.mapToMessageInfo(): MessageInfo {
    val textComment = parseComment()
    return when (val msgInfo = info) {
        is ExtInMsgInfo -> {
            MessageInfo.ExtInMsgInfo(
                fromAddress = msgInfo.src.toTonAddress(),
                toAddress = msgInfo.dest.toTonAddress(),
                textComment = textComment,
                importFee = msgInfo.importFee.amount.value.toBigNum()
            )
        }
        is ExtOutMsgInfo -> {
            MessageInfo.ExtOutMsgInfo(
                fromAddress = msgInfo.src.toTonAddress(),
                toAddress = msgInfo.dest.toTonAddress(),
                textComment = textComment,
                createdLt = msgInfo.createdLt.toLong()
            )
        }
        is IntMsgInfo -> {
            MessageInfo.IntMsgInfo(
                fromAddress = msgInfo.src.toTonAddress(),
                toAddress = msgInfo.dest.toTonAddress(),
                fwdFee = msgInfo.fwd_fee.amount.value.toBigNum(),
                ihrFee = msgInfo.ihr_fee.amount.value.toBigNum(),
                tonAmount = msgInfo.value.coins.amount.value.toBigNum(),
                bounce = msgInfo.bounce,
                bounced = msgInfo.bounced,
                textComment = textComment,
                createdLt = msgInfo.created_lt
            )
        }
    }
}
