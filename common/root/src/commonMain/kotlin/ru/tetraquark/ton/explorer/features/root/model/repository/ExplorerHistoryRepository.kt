package ru.tetraquark.ton.explorer.features.root.model.repository

import ru.tetraquark.ton.explorer.core.storage.SettingsStorage

class ExplorerHistoryRepository(
    private val storage: SettingsStorage,
    private val storageHistoryMaxSize: Int = 10
) {
    fun getExplorerAddressHistory(itemsCount: Int): List<String> {
        return storage.addressHistory?.take(itemsCount) ?: emptyList()
    }

    fun saveAddressToHistory(address: String) {
        val historyList = storage.addressHistory?.toMutableList() ?: mutableListOf()

        if (historyList.size >= storageHistoryMaxSize) {
            historyList.removeLast()
        }
        historyList.add(0, address)

        storage.addressHistory = historyList
    }
}
