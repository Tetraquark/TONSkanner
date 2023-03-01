package ru.tetraquark.ton.explorer.features.base.resources

import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.image.ImageDesc

class ErrorDesc(
    val message: StringDesc,
    val icon: ImageDesc? = null
)
