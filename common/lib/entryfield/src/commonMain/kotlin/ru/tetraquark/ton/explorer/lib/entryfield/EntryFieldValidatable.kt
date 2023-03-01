package ru.tetraquark.ton.explorer.lib.entryfield

abstract class EntryFieldValidatable<V, E>(
    val autoValidation: Boolean = false,
    protected val validation: ValidationRule<V, E>? = null,
) : EntryField<V, E> {
    val isValid: Boolean
        get() = errorState.value == null

    protected abstract fun newValue(value: V)

    final override fun setValue(value: V) {
        newValue(value)
        if (autoValidation) {
            validate()
        }
    }

    fun validate(): Boolean {
        validation?.validate(valueState.value)
            ?.run(::setError)

        return isValid
    }
}
