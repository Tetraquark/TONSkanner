package ru.tetraquark.ton.explorer.features.root.utils

import io.ktor.util.decodeBase64Bytes
import io.ktor.util.decodeBase64String
import ru.tetraquark.ton.explorer.lib.resources.imagedesc.ImageDescBase64

fun ImageDescBase64.decodeToByteArray(): ByteArray = data.decodeBase64Bytes()

fun ImageDescBase64.decodeToString(): String = data.decodeBase64String()

fun ImageDescBase64.isSvg(): Boolean = decodeToString().contains("<svg")
