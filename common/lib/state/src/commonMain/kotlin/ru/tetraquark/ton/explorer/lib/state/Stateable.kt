package ru.tetraquark.ton.explorer.lib.state

sealed interface Stateable<out V, out E> {
    object Loading : Stateable<Nothing, Nothing>

    class Failure<out E>(val failure: E) : Stateable<Nothing, E>

    class Success<V>(val data: V) : Stateable<V, Nothing> {
        inline fun newState(transform: (V) -> V): Success<V> {
            return Success(transform(this.data))
        }
    }
}

fun <V> Stateable<V, *>.getIfSuccess(): Stateable.Success<V>? = this as? Stateable.Success<V>

fun Stateable<*, *>.isLoading(): Boolean = this is Stateable.Loading
