package ru.tetraquark.ton.explorer.features.base.resources

fun interface ExceptionMapper {
    fun map(exception: Throwable): ErrorDesc

    operator fun invoke(exception: Throwable) = map(exception)
}
