package ru.tetraquark.ton.explorer.features.component.blockchain.entity

import com.soywiz.kbignum.BigNum
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc

data class CoinAmount(
    val amount: BigNum,
    val decimals: Int,
    val symbol: String
) {
    fun toStringDesc(): StringDesc {
        val amountString = amount.toString().let { str ->
            val intStr = str.dropLast(decimals).ifEmpty { "0" }
            val decStr = str.takeLast(decimals)
                .padStart(decimals, '0')
                .dropLastWhile { it == '0' }
            if (decStr.isEmpty()) {
                intStr
            } else {
                "$intStr.$decStr"
            }
        }

        return "$amountString $symbol".desc()
    }

    companion object
}

fun CoinAmount.Companion.ToncoinAmount(amount: BigNum): CoinAmount =
    CoinAmount(amount, 9, "TON")
