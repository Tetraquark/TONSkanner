package ru.tetraquark.ton.explorer.core.ton.utils

import org.ton.bitstring.BitString
import org.ton.block.Message
import org.ton.cell.Cell

internal fun Message<Cell>.parseComment(): String? {
    val firstCell = body.fold(
        left = { it },
        right = { it.value }
    )

    if (firstCell.bits.isEmpty()) return null

    return firstCell.beginParse().run {
        val op = loadUInt32().toInt()
        if (op == 0) {
            val firstCellBits = bits.slice(bitsPosition, bits.size)
            val commentBits = refs.firstOrNull()?.concatSnake()?.let {
                firstCellBits + BitString(it)
            } ?: firstCellBits
            commentBits.toByteArray().decodeToString()
        } else {
            null
        }
    }
}
