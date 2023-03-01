package ru.tetraquark.ton.explorer.app.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import com.seiko.imageloader.rememberAsyncImagePainter
import dev.icerock.moko.resources.desc.image.ImageDesc
import dev.icerock.moko.resources.desc.image.ImageDescResource
import dev.icerock.moko.resources.desc.image.ImageDescUrl
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.features.root.utils.decodeToByteArray
import ru.tetraquark.ton.explorer.features.root.utils.isSvg
import ru.tetraquark.ton.explorer.lib.resources.imagedesc.ImageDescBase64
import ru.tetraquark.ton.explorer.lib.resources.imagedesc.ImageDescBinary
import javax.imageio.ImageIO

@Composable
actual fun ImageDesc.toPainter(): Painter {
    return when (this) {
        is ImageDescUrl -> {
            rememberAsyncImagePainter(this.url)
        }
        is ImageDescResource -> {
            remember(this) { this.resource.image.toPainter() }
        }
        is ImageDescBase64 -> {
            remember(this) {
                if (isSvg()) {
                    loadSvgPainter(
                        inputStream = decodeToByteArray().inputStream(),
                        density = Density(1f)
                    )
                } else {
                    ImageIO.read(decodeToByteArray().inputStream())?.toPainter()
                }
            } ?: CustomTheme.drawables.gmailErrorred.toPainter()
        }
        is ImageDescBinary -> {
            remember(this) {
                ImageIO.read(data.inputStream())?.toPainter()
            } ?: CustomTheme.drawables.gmailErrorred.toPainter()
        }
        is ImageDescDrawable -> {
            painterResource("drawable/${filePath}.xml")
        }
        else -> {
            error("This kind of ImageDesc is not supported.")
        }
    }
}
