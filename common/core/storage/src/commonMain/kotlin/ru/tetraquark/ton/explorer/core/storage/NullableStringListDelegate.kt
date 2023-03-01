package ru.tetraquark.ton.explorer.core.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class NullableStringListDelegate(
    private val settings: Settings,
    private val json: Json,
    private val key: String?,
) : ReadWriteProperty<Any?, List<String>?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): List<String>? {
        val jsonString = settings.getStringOrNull(key ?: property.name) ?: return null
        return runCatching {
            json.decodeFromString(ListSerializer(String.serializer()), jsonString)
        }.getOrNull()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: List<String>?) {
        val listAsJson = value?.let {
            json.encodeToString(ListSerializer(String.serializer()), it)
        }

        settings.set(
            key = key ?: property.name,
            value = listAsJson
        )
    }
}

internal fun Settings.nullableStringList(
    key: String? = null,
    json: Json
): ReadWriteProperty<Any?, List<String>?> = NullableStringListDelegate(
    settings = this,
    json = json,
    key = key,
)
