package ru.tetraquark.ton.explorer.features.screen.transactionlist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.tetraquark.ton.explorer.core.ton.TonLiteClientApi
import ru.tetraquark.ton.explorer.features.base.decompose.asValue
import ru.tetraquark.ton.explorer.features.base.resources.ExceptionMapper
import ru.tetraquark.ton.explorer.features.base.viewmodel.ViewModelBase
import ru.tetraquark.ton.explorer.features.base.viewmodel.viewModel
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.TransactionsHistoryComponent
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.TransactionsHistoryFeature

class TransactionListScreenComponent(
    componentContext: ComponentContext,
    tonLiteClientApi: TonLiteClientApi,
    exceptionMapper: ExceptionMapper,
    val address: String,
    val routeBack: () -> Unit
) : TransactionListScreen, ComponentContext by componentContext {

    override val transactionsHistoryFeature: TransactionsHistoryFeature =
        TransactionsHistoryComponent(
            componentContext = childContext(key = "TransactionsHistoryComponent"),
            tonLiteClientApi = tonLiteClientApi,
            exceptionMapper = exceptionMapper,
            pageSize = 15
        )

    override val accountAddress: String
        get() = address

    private val viewModel = viewModel(instanceKeeper) { ViewModel() }

    override val screenState: Value<TransactionListScreen.ScreenState> =
        viewModel.state.asValue(viewModel.instanceScope)

    override fun onBackButtonClicked() {
        routeBack()
    }

    override fun onErrorShown() {
        viewModel.state.value = viewModel.state.value.copy(errorEvent = null)
    }

    private inner class ViewModel : ViewModelBase() {
        val state = MutableStateFlow(TransactionListScreen.ScreenState(null))

        init {
            instanceScope.launch {
                transactionsHistoryFeature.singleErrorFlow.collect {
                    state.value = state.value.copy(errorEvent = it)
                }
            }

            transactionsHistoryFeature.loadData(address)
        }
    }
}
