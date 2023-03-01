package ru.tetraquark.ton.explorer.features.root.validation

import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import ru.tetraquark.ton.explorer.features.root.MR
import ru.tetraquark.ton.explorer.lib.validation.Validation
import ru.tetraquark.ton.explorer.lib.validation.matchRegex
import ru.tetraquark.ton.explorer.lib.validation.minLength
import ru.tetraquark.ton.explorer.lib.validation.notEmpty
import ru.tetraquark.ton.explorer.lib.validation.validate

internal val TonAddressRegex = Regex("[UEk0][Qf][\\w\\-]{46}")

internal fun validateTONWalletAddressField(value: String): StringDesc? {
    return Validation.of<String, StringDesc>(value)
        .notEmpty(
            failure = MR.strings.common_validation_failure_empty.desc()
        )
        .minLength(
            failure = MR.strings.validation_incorrect_ton_address.desc(),
            minLength = 48
        )
        .matchRegex(
            failure = MR.strings.validation_incorrect_ton_address.desc(),
            TonAddressRegex
        )
        .validate()
}
