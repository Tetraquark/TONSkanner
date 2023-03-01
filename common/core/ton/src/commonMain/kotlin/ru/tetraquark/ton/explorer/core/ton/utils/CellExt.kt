package ru.tetraquark.ton.explorer.core.ton.utils

import org.ton.cell.Cell

/**
 * TEP-64 Token Data Standard
 */
internal fun Cell?.concatSnake(): ByteArray {
    if (this == null) return ByteArray(0)
    return concatBytes(
        refsWalk = ::snakeWalk
    )
}

internal fun Cell?.concatRefs(): ByteArray {
    if (this == null) return ByteArray(0)
    return concatBytes(
        refsWalk = ::walkCellRefs
    )
}

private inline fun walkCellRefs(startCell: Cell?, action: (Cell) -> Unit) {
    if (startCell == null) return
    action(startCell)
    startCell.treeWalk().forEach(action)
}

private fun snakeWalk(startCell: Cell?, action: (Cell) -> Unit) {
    if (startCell == null) return
    action(startCell)
    snakeWalk(startCell.refs.firstOrNull(), action)
}

private fun Cell.concatBytes(refsWalk: (Cell, (Cell) -> Unit) -> Unit): ByteArray {
    var length = 0
    refsWalk(this) { length += it.bits.toByteArray().size }

    val buffer = ByteArray(length)
    var offset = 0
    refsWalk(this) {
        val bits = it.bits.toByteArray()
        bits.forEachIndexed { index, byte ->
            buffer[index + offset] = byte
        }
        offset += bits.size
    }

    return buffer
}
