package ru.tetraquark.ton.explorer.features.screen.startexplorer

import com.arkivanov.decompose.value.Value
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc
import ru.tetraquark.ton.explorer.features.component.queryhistory.QueryHistoryFeature
import ru.tetraquark.ton.explorer.features.component.inputaddress.InputAddressFeature
import ru.tetraquark.ton.explorer.lib.state.StateWithError

interface StartExplorerScreen {
    val inputAddressFeature: InputAddressFeature
    val queryHistoryFeature: QueryHistoryFeature

    val screenState: Value<ScreenState>

    fun onErrorShown()

    data class ScreenState(
        override val errorEvent: ErrorDesc?
    ) : StateWithError<ErrorDesc>
}
