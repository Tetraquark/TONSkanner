package ru.tetraquark.ton.explorer.lib.validation

fun <E : Any> Validation<String, E>.notEmpty(failure: E) = nextValidation { value ->
    if (value.isEmpty()) {
        Validation.failure(failure)
    } else {
        Validation.success(value)
    }
}

fun <E : Any> Validation<String, E>.notBlank(failure: E) = nextValidation { value ->
    if (value.isBlank()) {
        Validation.failure(failure)
    } else {
        Validation.success(value)
    }
}

fun <E : Any> Validation<String, E>.matchRegex(
    failure: E,
    vararg regex: Regex
) = nextValidation { value ->
    val result = regex.all { it.matches(value) }

    if (result) {
        Validation.success(value)
    } else {
        Validation.failure(failure)
    }
}

fun <E : Any> Validation<String, E>.containsRegex(
    failure: E,
    vararg regex: Regex
) = nextValidation { value ->
    val result = regex.all { value.contains(it) }

    if (result) {
        Validation.success(value)
    } else {
        Validation.failure(failure)
    }
}

fun <E : Any> Validation<String, E>.maxLength(
    failure: E,
    maxLength: Int = 0
) = nextValidation { value ->
    if (value.length > maxLength) {
        Validation.failure(failure)
    } else {
        Validation.success(value)
    }
}

fun <E : Any> Validation<String, E>.minLength(
    failure: E,
    minLength: Int = 0
) = nextValidation { value ->
    if (value.length < minLength) {
        Validation.failure(failure)
    } else {
        Validation.success(value)
    }
}
