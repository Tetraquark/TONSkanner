package ru.tetraquark.ton.explorer.core.ton.utils

import org.ton.bitstring.Bits256
import org.ton.block.AddrExtern
import org.ton.block.AddrNone
import org.ton.block.AddrStd
import org.ton.block.AddrVar
import org.ton.block.Anycast
import org.ton.block.MsgAddress
import org.ton.block.toMaybe
import org.ton.cell.Cell
import org.ton.cell.CellSlice
import org.ton.contract.ContentData
import org.ton.contract.FullContent
import org.ton.tlb.exception.UnknownTlbConstructorException
import org.ton.tlb.loadTlb
import ru.tetraquark.ton.explorer.core.ton.entity.metadata.MetadataContent
import ru.tetraquark.ton.explorer.core.ton.exception.TonIncorrectCellException
import kotlin.math.ceil
import kotlin.math.log2

internal fun CellSlice?.loadMsgAddress(): MsgAddress? {
    if (this == null) return null

    val type = this.loadUInt(2).toInt()
    return when (type) {
        0 -> AddrNone
        1 -> {
            val len = this.loadUInt(9).toInt()
            val data = this.loadBits(len)
            AddrExtern(len, data)
        }
        2 -> {
            val isAnyCast = this.loadBit()
            val anycast = if (isAnyCast) {
                loadAddrAnycast()
            } else {
                null
            }
            val workchain = this.loadUInt(8).toInt()
            val data = this.loadBits(256)
            AddrStd(anycast = anycast.toMaybe(), workchainId = workchain, address = Bits256(data))
        }
        3 -> {
            val isAnyCast = this.loadBit()
            val anycast = if (isAnyCast) {
                loadAddrAnycast()
            } else {
                null
            }
            val len = this.loadUInt(9).toInt()
            val workchain = this.loadInt(32).toInt()
            val data = this.loadBits(len)
            AddrVar(
                anycast = anycast.toMaybe(),
                addrLen = len,
                workchainId = workchain,
                address = data
            )
        }
        else -> null
    }
}

private fun CellSlice.loadAddrAnycast(): Anycast {
    val len = ceil(log2(30f)).toInt()
    val depth = loadUInt(len).toInt()
    val pfx = loadBits(depth)
    return Anycast(depth, pfx)
}

internal fun <T> Cell.parseMetadataContent(
    onchainMetadataFactory: (Map<String, Any?>) -> T
): MetadataContent {
    val content = try {
        if (bits.isEmpty()) {
            refs.firstOrNull() ?: throw TonIncorrectCellException(this)
        } else {
            this
        }.beginParse().loadTlb(FullContent.tlbCombinator())
    } catch (e: UnknownTlbConstructorException) {
        return MetadataContent.OffChain(this.concatSnake().decodeToString())
    }

    return when (content) {
        is FullContent.OffChain -> {
            val bytes = content.uri.data.concat()
            MetadataContent.OffChain(bytes.decodeToString())
        }
        is FullContent.OnChain -> {
            val dataMap = content.data.map { pair ->
                val key = pair.first.toByteArray().decodeToString()
                val data = pair.second

                val dataAny: Any = when (data) {
                    is ContentData.Chunks -> data.data.data.map {
                        it.first.toByteArray().decodeToString() to it.second.bits.toByteArray()
                    }
                    is ContentData.Snake -> data.data.concat()
                }
                key to dataAny
            }.toMap()

            dataMap["uri"]?.let {
                MetadataContent.OffChain((it as ByteArray).decodeToString())
            } ?: MetadataContent.OnChain(
                metadata = onchainMetadataFactory(dataMap)
            )
        }
    }
}
