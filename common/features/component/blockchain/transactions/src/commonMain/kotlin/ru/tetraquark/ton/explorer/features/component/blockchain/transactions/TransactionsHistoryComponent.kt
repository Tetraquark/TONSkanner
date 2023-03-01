package ru.tetraquark.ton.explorer.features.component.blockchain.transactions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.tetraquark.ton.explorer.core.ton.TonLiteClientApi
import ru.tetraquark.ton.explorer.features.base.decompose.CoroutineScope
import ru.tetraquark.ton.explorer.features.base.decompose.asValue
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc
import ru.tetraquark.ton.explorer.features.base.resources.ExceptionMapper
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model.TransactionRepository
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model.entity.TransactionListItem
import ru.tetraquark.ton.explorer.lib.state.Stateable

class TransactionsHistoryComponent(
    componentContext: ComponentContext,
    tonLiteClientApi: TonLiteClientApi,
    private val exceptionMapper: ExceptionMapper,
    private val pageSize: Int = 15
) : TransactionsHistoryFeature, ComponentContext by componentContext {

    private val repository = TransactionRepository(tonLiteClientApi)

    private val store = instanceKeeper.getStore {
        TransactionsHistoryStoreProvider(
            storeFactory = DefaultStoreFactory(),
            transactionRepository = repository,
            exceptionMapper = exceptionMapper
        ).provide()
    }

    private val componentScope = CoroutineScope()

    override val state: Value<Stateable<TransactionsHistoryFeature.Model, ErrorDesc>> =
        store.stateFlow.map { state ->
            when {
                state.isInitLoading -> Stateable.Loading
                state.listState is TransactionsHistoryStore.State.DataState.Error -> Stateable.Failure(
                    state.listState.error
                )
                state.listState is TransactionsHistoryStore.State.DataState.ItemList -> Stateable.Success(
                    TransactionsHistoryFeature.Model(
                        itemList = state.listState.list, isPageLoading = state.isPageLoading
                    )
                )
                else -> error("Incorrect state")
            }
        }.stateIn(componentScope, SharingStarted.Lazily, Stateable.Loading).asValue(componentScope)

    override val singleErrorFlow: Flow<ErrorDesc> = store.labels

    override fun loadData(address: String) {
        store.accept(TransactionsHistoryStore.Intent.LoadFirstPage(address, pageSize))
    }

    override fun reloadData() {
        store.accept(TransactionsHistoryStore.Intent.Reload(pageSize))
    }

    override fun onItemClicked(item: TransactionListItem) {
        // TODO("Not yet implemented")
    }

    override fun loadNextPage() {
        store.accept(TransactionsHistoryStore.Intent.LoadNextPage(pageSize))
    }
}
