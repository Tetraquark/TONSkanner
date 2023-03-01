package ru.tetraquark.ton.explorer.features.component.inputaddress

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.consume
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.tetraquark.ton.explorer.core.storage.SettingsStorage
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc
import ru.tetraquark.ton.explorer.features.base.resources.ExceptionMapper
import ru.tetraquark.ton.explorer.features.base.viewmodel.ViewModelBase
import ru.tetraquark.ton.explorer.features.base.viewmodel.viewModel
import ru.tetraquark.ton.explorer.lib.entryfield.TextEntryField
import ru.tetraquark.ton.explorer.lib.entryfield.ValidationRule

class InputAddressComponent(
    componentContext: ComponentContext,
    private val storage: SettingsStorage,
    private val walletAddressValidationRule: ValidationRule<String, StringDesc>,
    private val startAddressExplorer: suspend (String) -> Unit,
    private val exceptionMapper: ExceptionMapper,
    private val explorerHistoryStorageMaxSize: Int,
    initAddress: String? = null,
) : InputAddressFeature, ComponentContext by componentContext {
    private val viewModel = viewModel(instanceKeeper) {
        InputAddressViewModel(
            initAddress = initAddress.orEmpty(),
            walletAddressValidationRule = walletAddressValidationRule,
            explorerHistoryStorageMaxSize = explorerHistoryStorageMaxSize,
            startAddressExplorer = startAddressExplorer,
            storage = storage,
            stateKeeper = stateKeeper,
            exceptionMapper = exceptionMapper
        )
    }

    override val addressField: TextEntryField<StringDesc>
        get() = viewModel.addressTextField

    override val isExplorerLoading: Value<Boolean>
        get() = viewModel.isExplorerLoading

    override val singleErrorFlow: Flow<ErrorDesc>
        get() = viewModel.errorFlow

    override fun onContinueButtonClicked() {
        if (!viewModel.addressTextField.validate()) return
        viewModel.startExplorer(viewModel.addressTextField.valueState.value)
    }

    override fun onQRCodeButtonClicked() {
        // TODO("Not yet implemented")
    }

    override fun startExplorer(address: String) {
        viewModel.addressTextField.setValue(address)
        if (!viewModel.addressTextField.validate()) return
        viewModel.startExplorer(address)
    }
}
