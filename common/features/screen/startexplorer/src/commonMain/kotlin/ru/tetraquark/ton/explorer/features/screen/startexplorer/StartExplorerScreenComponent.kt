package ru.tetraquark.ton.explorer.features.screen.startexplorer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tetraquark.ton.explorer.core.storage.SettingsStorage
import ru.tetraquark.ton.explorer.features.base.resources.ExceptionMapper
import ru.tetraquark.ton.explorer.features.base.viewmodel.ViewModelBase
import ru.tetraquark.ton.explorer.features.base.viewmodel.viewModel
import ru.tetraquark.ton.explorer.features.component.queryhistory.QueryHistoryComponent
import ru.tetraquark.ton.explorer.features.component.queryhistory.QueryHistoryFeature
import ru.tetraquark.ton.explorer.features.component.inputaddress.InputAddressComponent
import ru.tetraquark.ton.explorer.features.component.inputaddress.InputAddressFeature
import ru.tetraquark.ton.explorer.lib.entryfield.ValidationRule

class StartExplorerScreenComponent(
    componentContext: ComponentContext,
    storage: SettingsStorage,
    exceptionMapper: ExceptionMapper,
    private val initTonLiteClient: suspend () -> Unit,
    private val routeNextScreen: (String) -> Unit,
    walletAddressValidationRule: ValidationRule<String, StringDesc>,
    explorerHistoryStorageMaxSize: Int,
) : StartExplorerScreen, ComponentContext by componentContext {
    override val inputAddressFeature: InputAddressFeature = InputAddressComponent(
        componentContext = childContext(key = "InputWalletAddressComponent"),
        storage = storage,
        walletAddressValidationRule = walletAddressValidationRule,
        startAddressExplorer = ::openExplorerMainForAddress,
        exceptionMapper = exceptionMapper,
        explorerHistoryStorageMaxSize = explorerHistoryStorageMaxSize,
        initAddress = null
    )

    override val queryHistoryFeature: QueryHistoryFeature = QueryHistoryComponent(
        componentContext = childContext(key = "ExplorerHistoryComponent"),
        storage = storage,
        runAddressExplorer = { address ->
            inputAddressFeature.run {
                addressField.setValue(address)
                onContinueButtonClicked()
            }
        },
        historySize = 5
    )

    private val viewModel = viewModel(instanceKeeper, ::ViewModel)

    override val screenState: Value<StartExplorerScreen.ScreenState>
        get() = viewModel.screenState

    override fun onErrorShown() {
        viewModel.screenState.value = viewModel.screenState.value.copy(errorEvent = null)
    }

    private suspend fun openExplorerMainForAddress(address: String) {
        withContext(Dispatchers.IO) { initTonLiteClient() }
        routeNextScreen(address)
    }

    internal inner class ViewModel : ViewModelBase() {
        val screenState = MutableValue(StartExplorerScreen.ScreenState(null))

        init {
            instanceScope.launch {
                inputAddressFeature.singleErrorFlow.collect {
                    screenState.value = screenState.value.copy(errorEvent = it)
                }
            }
        }
    }
}
