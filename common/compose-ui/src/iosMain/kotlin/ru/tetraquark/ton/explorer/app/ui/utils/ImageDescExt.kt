package ru.tetraquark.ton.explorer.app.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.rememberAsyncImagePainter
import com.seiko.imageloader.toPainter
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.desc.image.ImageDesc
import dev.icerock.moko.resources.desc.image.ImageDescResource
import dev.icerock.moko.resources.desc.image.ImageDescUrl
import org.jetbrains.skia.Image
import ru.tetraquark.ton.explorer.features.root.utils.decodeToByteArray
import ru.tetraquark.ton.explorer.features.root.utils.decodeToString
import ru.tetraquark.ton.explorer.features.root.utils.isSvg
import ru.tetraquark.ton.explorer.lib.resources.imagedesc.ImageDescBase64
import ru.tetraquark.ton.explorer.lib.resources.imagedesc.ImageDescBinary

@Composable
actual fun ImageDesc.toPainter(): Painter {
    return when (this) {
        is ImageDescUrl -> {
            rememberAsyncImagePainter(this.url)
        }

        is ImageDescResource -> {
            painterResource(this.resource)
        }

        is ImageDescBase64 -> {
            if (isSvg()) {
                rememberAsyncImagePainter(
                    ImageRequest(decodeToString())
                )
            } else {
                remember(this) {
                    Image.makeFromEncoded(decodeToByteArray()).toPainter()
                }
            }
        }

        is ImageDescBinary -> {
            remember(this) {
                Image.makeFromEncoded(this.data).toPainter()
            }
        }

        is ImageDescDrawable -> {
            TODO("Not implemented")
            //painterResource("drawable/${filePath}")
        }

        else -> {
            error("This kind of ImageDesc is not supported.")
        }
    }
}
