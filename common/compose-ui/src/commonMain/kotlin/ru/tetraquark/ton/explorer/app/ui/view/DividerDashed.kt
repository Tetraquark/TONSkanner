package ru.tetraquark.ton.explorer.app.ui.view

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme

@Composable
fun DividerDashed(
    modifier: Modifier = Modifier,
    color: Color = CustomTheme.colors.outline,
    strokeWidth: Dp = 2.dp,
    intervals: FloatArray? = null,
    phase: Dp = 3.dp,
) {
    val _intervals = intervals ?: floatArrayOf(7.dp.px(), 7.dp.px())
    val _phase = phase.px()
    Canvas(
        modifier = modifier
    ) {
        drawLine(
            color = color,
            start = Offset.Zero,
            end = Offset(size.width - 1, 0f),
            strokeWidth = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                _intervals, _phase
            )
        )
    }
}
