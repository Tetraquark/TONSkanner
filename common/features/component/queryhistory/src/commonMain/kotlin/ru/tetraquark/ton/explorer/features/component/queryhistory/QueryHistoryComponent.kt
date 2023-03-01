package ru.tetraquark.ton.explorer.features.component.queryhistory

import com.arkivanov.decompose.ComponentContext
import ru.tetraquark.ton.explorer.core.storage.SettingsStorage

class QueryHistoryComponent(
    componentContext: ComponentContext,
    storage: SettingsStorage,
    private val runAddressExplorer: (String) -> Unit,
    historySize: Int,
) : QueryHistoryFeature, ComponentContext by componentContext {

    override val addressesHistory: List<String> =
        storage.addressHistory?.take(historySize) ?: emptyList()

    override fun onHistoryAddressClicked(index: Int) {
        addressesHistory.getOrNull(index)?.apply(runAddressExplorer)
    }
}
