package ru.tetraquark.ton.explorer.features.screen.explorermain

import com.arkivanov.decompose.value.Value
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc
import ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo.AddressInfoFeature
import ru.tetraquark.ton.explorer.features.component.inputaddress.InputAddressFeature
import ru.tetraquark.ton.explorer.lib.state.StateWithError

interface ExplorerMainScreen {
    val inputAddressFeature: InputAddressFeature
    val addressInfoFeature: AddressInfoFeature

    val screenState: Value<ScreenState>

    fun onErrorShown()

    fun onTransactionListClicked()

    data class ScreenState(
        override val errorEvent: ErrorDesc?
    ) : StateWithError<ErrorDesc>
}
