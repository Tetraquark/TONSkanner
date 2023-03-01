package ru.tetraquark.ton.explorer.lib.validation

fun <V : Any, E : Any> Validation<V?, E>.notNull(failure: E) = nextValidation { value ->
    if (value == null) {
        Validation.failure(failure)
    } else {
        Validation.success(value)
    }
}
