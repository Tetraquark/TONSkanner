package ru.tetraquark.ton.explorer.features.component.inputaddress

import com.arkivanov.decompose.value.Value
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.Flow
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc
import ru.tetraquark.ton.explorer.lib.entryfield.TextEntryField

interface InputAddressFeature {
    val addressField: TextEntryField<StringDesc>

    val isExplorerLoading: Value<Boolean>

    val singleErrorFlow: Flow<ErrorDesc>

    fun onContinueButtonClicked()

    fun onQRCodeButtonClicked()

    fun startExplorer(address: String)
}
