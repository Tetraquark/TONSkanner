package ru.tetraquark.ton.explorer.core.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.boolean
import com.russhwolf.settings.nullableBoolean
import com.russhwolf.settings.nullableString
import kotlinx.serialization.json.Json

internal class SettingsStorageImpl(settings: Settings, json: Json) : SettingsStorage {
    override var addressesHistoryJson: String? by settings.nullableString("addresses_history")

    override var addressHistory: List<String>? by settings.nullableStringList(
        key = "addresses_history",
        json = json,
    )

    override var isDarkModeTheme: Boolean? by settings.nullableBoolean("is_dark_theme")

    override fun clear() {
        addressesHistoryJson = null
    }
}
