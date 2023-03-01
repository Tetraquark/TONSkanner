package ru.tetraquark.ton.explorer.features.screen.explorermain

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.launch
import ru.tetraquark.ton.explorer.core.storage.SettingsStorage
import ru.tetraquark.ton.explorer.core.ton.TonLiteClientApi
import ru.tetraquark.ton.explorer.features.base.resources.ExceptionMapper
import ru.tetraquark.ton.explorer.features.base.viewmodel.ViewModelBase
import ru.tetraquark.ton.explorer.features.base.viewmodel.viewModel
import ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo.AddressInfoComponent
import ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo.AddressInfoFeature
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.Address
import ru.tetraquark.ton.explorer.features.component.inputaddress.InputAddressComponent
import ru.tetraquark.ton.explorer.features.component.inputaddress.InputAddressFeature
import ru.tetraquark.ton.explorer.lib.entryfield.ValidationRule
import ru.tetraquark.ton.explorer.lib.state.getIfSuccess

class ExplorerMainScreenComponent(
    componentContext: ComponentContext,
    private val storage: SettingsStorage,
    tonLiteClientApi: TonLiteClientApi,
    exceptionMapper: ExceptionMapper,
    walletAddressValidationRule: ValidationRule<String, StringDesc>,
    initAddress: String,
    explorerHistoryStorageMaxSize: Int,
    private val routeToTransactionList: (String) -> Unit
) : ExplorerMainScreen, ComponentContext by componentContext {

    override val inputAddressFeature: InputAddressFeature = InputAddressComponent(
        componentContext = childContext(key = "InputAddressComponent"),
        storage = storage,
        walletAddressValidationRule = walletAddressValidationRule,
        startAddressExplorer = ::loadAddress,
        exceptionMapper = exceptionMapper,
        explorerHistoryStorageMaxSize = explorerHistoryStorageMaxSize,
        initAddress = initAddress,
    )

    override val addressInfoFeature: AddressInfoFeature = AddressInfoComponent(
        componentContext = childContext(key = "AddressInfoComponent"),
        tonLiteClientApi = tonLiteClientApi,
        loadNewAddress = inputAddressFeature::startExplorer,
        exceptionMapper = exceptionMapper
    )

    private val viewModel = viewModel(instanceKeeper) {
        ViewModel(inputAddressFeature.addressField.valueState.value)
    }

    override val screenState: Value<ExplorerMainScreen.ScreenState>
        get() = viewModel.screenState

    override fun onErrorShown() {
        viewModel.screenState.value = viewModel.screenState.value.copy(errorEvent = null)
    }

    override fun onTransactionListClicked() {
        val address = inputAddressFeature.addressField.valueState.value
        routeToTransactionList(address)
    }

    private fun loadAddress(address: String) {
        viewModel.reloadWithAddress(address)
    }

    internal inner class ViewModel(initAddress: String) : ViewModelBase() {
        val screenState = MutableValue(
            ExplorerMainScreen.ScreenState(null)
        )

        init {
            instanceScope.launch {
                inputAddressFeature.singleErrorFlow.collect {
                    screenState.value = screenState.value.copy(errorEvent = it)
                }
            }

            reloadWithAddress(initAddress)
        }

        fun reloadWithAddress(address: String) {
            val currentAddress = addressInfoFeature.state.value.getIfSuccess()
                ?.data?.addressInfo?.address
            if (currentAddress == Address.SimpleAddress(address)) return

            addressInfoFeature.loadData(address)
        }
    }
}
