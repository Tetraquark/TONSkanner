package ru.tetraquark.ton.explorer.features.component.blockchain.transactions

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model.entity.TransactionListItem
import ru.tetraquark.ton.explorer.lib.state.Stateable

interface TransactionsHistoryFeature {
    val state: Value<Stateable<Model, ErrorDesc>>

    val singleErrorFlow: Flow<ErrorDesc>

    fun onItemClicked(item: TransactionListItem)

    fun loadData(address: String)

    fun loadNextPage()

    fun reloadData()

    data class Model(
        val itemList: List<TransactionListItem>,
        val isPageLoading: Boolean
    )
}
