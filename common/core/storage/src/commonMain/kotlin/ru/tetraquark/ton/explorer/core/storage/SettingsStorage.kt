package ru.tetraquark.ton.explorer.core.storage

import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json

interface SettingsStorage {
    var addressesHistoryJson: String?

    var addressHistory: List<String>?

    var isDarkModeTheme: Boolean?

    fun clear()
}

fun SettingsStorage(settings: Settings, json: Json): SettingsStorage =
    SettingsStorageImpl(settings, json)
