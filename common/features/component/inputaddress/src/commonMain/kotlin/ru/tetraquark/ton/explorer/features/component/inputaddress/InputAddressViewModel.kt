package ru.tetraquark.ton.explorer.features.component.inputaddress

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.consume
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.tetraquark.ton.explorer.core.storage.SettingsStorage
import ru.tetraquark.ton.explorer.features.base.TextEntryFieldValue
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc
import ru.tetraquark.ton.explorer.features.base.resources.ExceptionMapper
import ru.tetraquark.ton.explorer.features.base.viewmodel.ViewModelBase
import ru.tetraquark.ton.explorer.lib.entryfield.ValidationRule

internal class InputAddressViewModel(
    initAddress: String = "",
    walletAddressValidationRule: ValidationRule<String, StringDesc>,
    private val explorerHistoryStorageMaxSize: Int,
    private val startAddressExplorer: suspend (String) -> Unit,
    private val storage: SettingsStorage,
    stateKeeper: StateKeeper,
    private val exceptionMapper: ExceptionMapper,
) : ViewModelBase() {

    internal val addressTextField = TextEntryFieldValue(
        coroutineScope = instanceScope,
        initialValue = (stateKeeper.consume(STATE_KEEPER_KEY) as? AddressState)?.address
            ?: initAddress,
        validation = walletAddressValidationRule
    )

    internal val isExplorerLoading = MutableValue(false)
    internal val errorFlow = MutableSharedFlow<ErrorDesc>()

    init {
        stateKeeper.register(STATE_KEEPER_KEY) {
            AddressState(addressTextField.valueState.value)
        }
    }

    fun startExplorer(address: String) {
        if (isExplorerLoading.value) return
        isExplorerLoading.value = true

        instanceScope.launch {
            try {
                startAddressExplorer(address)
                saveAddressToExplorerHistory(address)
            } catch (ex: Exception) {
                errorFlow.emit(exceptionMapper(ex))
            } finally {
                isExplorerLoading.value = false
            }
        }
    }

    private fun saveAddressToExplorerHistory(address: String) {
        val historyList = storage.addressHistory?.toMutableList() ?: mutableListOf()

        if (historyList.size >= explorerHistoryStorageMaxSize) {
            historyList.removeLast()
        }
        historyList.add(0, address)

        storage.addressHistory = historyList
    }

    @Parcelize
    internal data class AddressState(
        val address: String? = null
    ) : Parcelable

    companion object {
        private const val STATE_KEEPER_KEY = "ADDRESS_INFO_STATE"
    }
}
