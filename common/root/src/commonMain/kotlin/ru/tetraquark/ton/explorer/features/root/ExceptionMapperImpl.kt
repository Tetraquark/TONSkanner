package ru.tetraquark.ton.explorer.features.root

import dev.icerock.moko.resources.desc.desc
import ru.tetraquark.ton.explorer.core.ton.exception.TonIncorrectAddressException
import ru.tetraquark.ton.explorer.core.ton.exception.TonRepositoryInitException
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc
import ru.tetraquark.ton.explorer.features.base.resources.ExceptionMapper

object ExceptionMapperImpl : ExceptionMapper {
    override fun map(exception: Throwable): ErrorDesc {
        exception.printStackTrace()
        return when (exception) {
            is TonRepositoryInitException -> ErrorDesc(MR.strings.error_ton_init_connect.desc())
            is TonIncorrectAddressException -> ErrorDesc(MR.strings.error_ton_incorrect_address.desc())
            is IllegalStateException -> ErrorDesc(MR.strings.error_incorrect_state.desc())
            else -> ErrorDesc(MR.strings.common_unknown_error.desc())
        }
    }
}
