package ru.tetraquark.ton.explorer.features.base

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.tetraquark.ton.explorer.features.base.decompose.asValue
import ru.tetraquark.ton.explorer.lib.entryfield.TextEntryField
import ru.tetraquark.ton.explorer.lib.entryfield.ValidationRule

class TextEntryFieldValue<E : Any>(
    coroutineScope: CoroutineScope,
    initialValue: String,
    autoValidation: Boolean = false,
    isEnabled: Boolean = true,
    validation: ValidationRule<String, E>? = null
) : TextEntryField<E>(initialValue, autoValidation, isEnabled, validation) {
    val valueValue: Value<String> = valueState.asValue(coroutineScope)
    val failureValue: Value<OptionalValue<out E>> = errorState.map(::OptionalValue)
        .stateIn(coroutineScope, SharingStarted.Lazily, OptionalValue(null)).asValue(coroutineScope)
    val isEnabledValue: Value<Boolean> = super.isEnabled.asValue(coroutineScope)
}
