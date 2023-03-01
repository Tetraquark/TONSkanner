package ru.tetraquark.ton.explorer.features.screen.transactionlist

import com.arkivanov.decompose.value.Value
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.TransactionsHistoryFeature
import ru.tetraquark.ton.explorer.lib.state.StateWithError

interface TransactionListScreen {
    val transactionsHistoryFeature: TransactionsHistoryFeature

    val screenState: Value<ScreenState>

    val accountAddress: String

    fun onBackButtonClicked()

    fun onErrorShown()

    data class ScreenState(
        override val errorEvent: ErrorDesc?
    ) : StateWithError<ErrorDesc>
}
