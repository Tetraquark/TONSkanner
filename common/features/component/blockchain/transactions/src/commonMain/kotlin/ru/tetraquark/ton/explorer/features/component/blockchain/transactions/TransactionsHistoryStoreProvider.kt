package ru.tetraquark.ton.explorer.features.component.blockchain.transactions

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc
import ru.tetraquark.ton.explorer.features.base.resources.ExceptionMapper
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model.TransactionRepository
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model.entity.TransactionListItem

internal interface TransactionsHistoryStore :
    Store<TransactionsHistoryStore.Intent, TransactionsHistoryStore.State, ErrorDesc> {
    sealed class Intent {
        data class LoadFirstPage(val address: String, val size: Int) : Intent()
        data class Reload(val size: Int) : Intent()
        data class LoadNextPage(val size: Int) : Intent()
    }

    data class State(
        val listState: DataState,
        val isInitLoading: Boolean,
        val isPageLoading: Boolean,
        internal val isAllTransactionsLoaded: Boolean,
        internal val address: String,
    ) {
        sealed class DataState {
            data class ItemList(val list: List<TransactionListItem>) : DataState()
            data class Error(val error: ErrorDesc) : DataState()
        }
    }
}

internal class TransactionsHistoryStoreProvider(
    private val storeFactory: StoreFactory,
    private val transactionRepository: TransactionRepository,
    private val exceptionMapper: ExceptionMapper,
) {
    fun provide(): TransactionsHistoryStore {
        return object : TransactionsHistoryStore,
            Store<TransactionsHistoryStore.Intent, TransactionsHistoryStore.State, ErrorDesc> by
            storeFactory.create(
                name = TransactionsHistoryStore::class.simpleName,
                initialState = TransactionsHistoryStore.State(
                    listState = TransactionsHistoryStore.State.DataState.ItemList(emptyList()),
                    isInitLoading = true,
                    isPageLoading = false,
                    isAllTransactionsLoaded = false,
                    address = "",
                ),
                bootstrapper = SimpleBootstrapper(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}
    }

    private sealed class Msg {
        object InitLoading : Msg()
        data class InitItemsLoaded(val address: String, val list: List<TransactionListItem>) : Msg()
        data class InitLoadingError(val address: String, val error: ErrorDesc) : Msg()
        data class PageLoaded(val list: List<TransactionListItem>) : Msg()
        object PageLoading : Msg()
        object PageLoadingError : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<
            TransactionsHistoryStore.Intent,
            Unit,
            TransactionsHistoryStore.State,
            Msg,
            ErrorDesc>() {
        override fun executeIntent(
            intent: TransactionsHistoryStore.Intent,
            getState: () -> TransactionsHistoryStore.State
        ) {
            val currentState = getState()
            when (intent) {
                is TransactionsHistoryStore.Intent.LoadFirstPage,
                is TransactionsHistoryStore.Intent.Reload -> {
                    var address = ""
                    var size = 1
                    when {
                        intent is TransactionsHistoryStore.Intent.LoadFirstPage -> {
                            address = intent.address
                            size = intent.size
                        }
                        intent is TransactionsHistoryStore.Intent.Reload -> {
                            address = currentState.address
                            size = intent.size
                        }
                    }

                    dispatch(Msg.InitLoading)
                    scope.launch {
                        loadPage(
                            address = address,
                            size = size,
                            ltPair = null
                        ).fold(
                            onSuccess = {
                                dispatch(Msg.InitItemsLoaded(address, it))
                            },
                            onFailure = {
                                dispatch(Msg.InitLoadingError(address, exceptionMapper(it)))
                            }
                        )
                    }
                }
                is TransactionsHistoryStore.Intent.LoadNextPage -> {
                    if (currentState.isPageLoading || currentState.isAllTransactionsLoaded) return

                    val itemListState = currentState.listState
                            as? TransactionsHistoryStore.State.DataState.ItemList ?: return
                    if (itemListState.list.isEmpty()) return

                    dispatch(Msg.PageLoading)
                    scope.launch {
                        loadPage(
                            address = currentState.address,
                            size = intent.size,
                            ltPair = itemListState.list.last().ltInfo,
                        ).fold(
                            onSuccess = {
                                dispatch(Msg.PageLoaded(it))
                            },
                            onFailure = {
                                publish(exceptionMapper(it))
                                dispatch(Msg.PageLoadingError)
                            }
                        )
                    }
                }
            }
        }

        private suspend fun loadPage(
            address: String,
            size: Int,
            ltPair: Pair<Long, ByteArray>?,
        ): Result<List<TransactionListItem>> {
            return runCatching {
                transactionRepository.loadTransactions(
                    address = address,
                    size = size,
                    from = ltPair,
                )
            }
        }
    }

    private object ReducerImpl : Reducer<TransactionsHistoryStore.State, Msg> {
        override fun TransactionsHistoryStore.State.reduce(
            msg: Msg
        ): TransactionsHistoryStore.State {
            return when (msg) {
                is Msg.InitLoading -> {
                    copy(
                        isInitLoading = true
                    )
                }
                is Msg.InitItemsLoaded -> {
                    copy(
                        listState = TransactionsHistoryStore.State.DataState.ItemList(msg.list),
                        isAllTransactionsLoaded = msg.list.isEmpty(),
                        isInitLoading = false,
                        address = msg.address
                    )
                }
                is Msg.InitLoadingError -> {
                    copy(
                        listState = TransactionsHistoryStore.State.DataState.Error(msg.error),
                        isInitLoading = false,
                        address = msg.address
                    )
                }
                is Msg.PageLoaded -> {
                    val oldList =
                        (listState as TransactionsHistoryStore.State.DataState.ItemList).list
                    copy(
                        listState = TransactionsHistoryStore.State.DataState.ItemList(
                            oldList + msg.list
                        ),
                        isAllTransactionsLoaded = msg.list.isEmpty(),
                        isPageLoading = false,
                    )
                }
                is Msg.PageLoading -> {
                    copy(
                        isPageLoading = true
                    )
                }
                is Msg.PageLoadingError -> {
                    copy(
                        isPageLoading = false,
                    )
                }
            }
        }
    }
}
