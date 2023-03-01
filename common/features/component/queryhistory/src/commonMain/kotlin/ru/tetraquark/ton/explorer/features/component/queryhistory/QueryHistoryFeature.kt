package ru.tetraquark.ton.explorer.features.component.queryhistory

interface QueryHistoryFeature {
    val addressesHistory: List<String>

    fun onHistoryAddressClicked(index: Int)
}
