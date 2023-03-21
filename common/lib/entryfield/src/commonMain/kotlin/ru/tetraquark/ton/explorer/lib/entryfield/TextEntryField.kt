package ru.tetraquark.ton.explorer.lib.entryfield

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class TextEntryField<E : Any>(
    initialValue: String,
    autoValidation: Boolean = false,
    isEnabled: Boolean = true,
    validation: ValidationRule<String, E>? = null
) : EntryFieldValidatable<String, E>(
    autoValidation = autoValidation,
    validation = validation
) {
    private val _valueState = MutableStateFlow(initialValue)
    override val valueState: StateFlow<String>
        get() = _valueState

    private val _errorState = MutableStateFlow<E?>(null)
    override val errorState: StateFlow<E?>
        get() = _errorState

    private val _isEnabled = MutableStateFlow(isEnabled)
    override val isEnabled: StateFlow<Boolean>
        get() = _isEnabled

    constructor(
        autoValidation: Boolean = false,
        isEnabled: Boolean = true,
        validation: ValidationRule<String, E>? = null
    ) : this("", autoValidation, isEnabled, validation)

    override fun newValue(value: String) {
        _valueState.value = value
        if (_errorState.value != null) {
            _errorState.value = null
        }
    }

    override fun setError(error: E?) {
        _errorState.value = error
    }

    override fun setIsEnabled(isEnabled: Boolean) {
        _isEnabled.value = isEnabled
    }
}
