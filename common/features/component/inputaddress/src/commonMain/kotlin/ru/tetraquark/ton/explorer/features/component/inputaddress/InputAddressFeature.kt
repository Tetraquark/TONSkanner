package ru.tetraquark.ton.explorer.features.component.inputaddress

import com.arkivanov.decompose.value.Value
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.Flow
import ru.tetraquark.ton.explorer.features.base.TextEntryFieldValue
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc

interface InputAddressFeature {
    val addressField: TextEntryFieldValue<StringDesc>

    val isExplorerLoading: Value<Boolean>

    val singleErrorFlow: Flow<ErrorDesc>

    fun onContinueButtonClicked()

    fun onQRCodeButtonClicked()

    fun startExplorer(address: String)
}
