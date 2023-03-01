package ru.tetraquark.ton.explorer.app.ui.utils

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import dev.icerock.moko.resources.desc.image.ImageDesc
import dev.icerock.moko.resources.desc.image.ImageDescResource
import dev.icerock.moko.resources.desc.image.ImageDescUrl
import ru.tetraquark.ton.explorer.features.root.utils.decodeToByteArray
import ru.tetraquark.ton.explorer.features.root.utils.isSvg
import ru.tetraquark.ton.explorer.lib.resources.imagedesc.ImageDescBase64
import ru.tetraquark.ton.explorer.lib.resources.imagedesc.ImageDescBinary

@Composable
actual fun ImageDesc.toPainter(): Painter {
    return when (this) {
        is ImageDescUrl -> {
            rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .build()
            )
        }
        is ImageDescResource -> {
            painterResource(resource.drawableResId)
        }
        is ImageDescBase64 -> {
            if (isSvg()) {
                rememberAsyncImagePainter(data) // resolved by SvgDecoder
            } else {
                val bytes = decodeToByteArray()
                rememberAsyncImagePainter(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            }
        }
        is ImageDescBinary -> {
            val bytes = data
            rememberAsyncImagePainter(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
        }
        is ImageDescDrawable -> {
            painterResource(resourceId)
        }
        else -> {
            error("This kind of ImageDesc is not supported.")
        }
    }
}
