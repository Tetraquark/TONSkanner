package ru.tetraquark.ton.explorer.core.ton.utils

import org.ton.block.AddrStd
import org.ton.block.DepthBalanceInfo
import org.ton.block.Either
import org.ton.block.ShardAccount
import org.ton.block.ShardState
import org.ton.block.ShardStateUnsplit
import org.ton.block.SplitState
import org.ton.boc.BagOfCells
import org.ton.cell.CellType
import org.ton.contract.SnakeData
import org.ton.contract.SnakeDataCons
import org.ton.contract.SnakeDataTail
import org.ton.hashmap.HashmapAug
import org.ton.hashmap.HashmapAugE
import org.ton.hashmap.HashmapAugNode
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerAccountState
import org.ton.tlb.CellRef
import org.ton.tlb.loadTlb
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
internal inline fun <X, Y, R> Either<X, Y>.fold(
    left: (X) -> R,
    right: (Y) -> R,
): R {
    contract {
        callsInPlace(left, InvocationKind.AT_MOST_ONCE)
        callsInPlace(right, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Either.Left -> left(this.value)
        is Either.Right -> right(this.value)
    }
}

internal fun LiteServerAccountId(addrStd: AddrStd): LiteServerAccountId =
    LiteServerAccountId(addrStd.workchainId, addrStd.address)

internal fun SnakeData.concat(): ByteArray {
    return when (this) {
        is SnakeDataCons -> this.bits.toByteArray() + this.next.concat()
        is SnakeDataTail -> this.bits.toByteArray()
    }
}

internal fun LiteServerAccountState.parseShardAccount(): ShardAccount? {
    var shardAccount: ShardAccount? = null
    BagOfCells(proof).first().beginParse().run {
        val slice = loadRef().beginParse()
        when (val shardState = slice.loadTlb(ShardState)) {
            is SplitState -> {
                shardState.right
                shardAccount = null
            }
            is ShardStateUnsplit -> {
                val root = shardState.accounts.value.x as HashmapAugE.AhmeRoot<ShardAccount, DepthBalanceInfo>
                root.walk { ahmEdge ->
                    shardAccount = (ahmEdge.node as HashmapAugNode.AhmnLeaf<ShardAccount, DepthBalanceInfo>).value
                }
            }
        }
    }
    return shardAccount
}

internal fun <X, Y> HashmapAugE<X, Y>.walk(action: (HashmapAug.AhmEdge<X, Y>) -> Unit) {
    val root = when (this) {
        is HashmapAugE.AhmeRoot -> this
        else -> return
    }

    val edges = mutableListOf<HashmapAug.AhmEdge<X, Y>>()
    val rootEdge = root.loadRoot() as HashmapAug.AhmEdge<X, Y>
    edges.add(rootEdge)

    while (edges.isNotEmpty()) {
        val edge = edges.removeFirst()
        when (val node = edge.node) {
            is HashmapAugNode.AhmnFork -> {
                node.left.getEdgeIfOrdinary()?.run(edges::add)
                node.right.getEdgeIfOrdinary()?.run(edges::add)
            }
            is HashmapAugNode.AhmnLeaf -> {
                action(edge)
            }
        }
    }
}

private fun <X, Y> CellRef<HashmapAug<X, Y>>.getEdgeIfOrdinary(): HashmapAug.AhmEdge<X, Y>? {
    return if (toCell().type == CellType.ORDINARY) {
        value as HashmapAug.AhmEdge<X, Y>
    } else {
        null
    }
}
