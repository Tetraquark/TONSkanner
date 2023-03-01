package ru.tetraquark.ton.explorer.lib.validation

sealed class Validation<out V : Any?, out E : Any> {

    open fun <T : Any?, E : Any> Validation<T, E>.nextValidation(
        block: (value: T) -> Validation<T, E>
    ): Validation<T, E> {
        return if (this is Success) {
            block(this.value)
        } else {
            this
        }
    }

    class Success<out V : Any?> internal constructor(val value: V) : Validation<V, Nothing>()
    class Failure<out E : Any> internal constructor(val failure: E) : Validation<Nothing, E>()

    companion object {
        fun <V : Any?> success(value: V) = Success(value)

        fun <E : Any> failure(failure: E) = Failure(failure)

        fun <V : Any?, E : Any> of(value: V): Validation<V, E> {
            return success(value)
        }
    }
}

fun <V : Any?, E : Any> Validation<V, E>.validate(): E? {
    return when (this) {
        is Validation.Failure -> {
            this.failure
        }
        else -> {
            null
        }
    }
}
