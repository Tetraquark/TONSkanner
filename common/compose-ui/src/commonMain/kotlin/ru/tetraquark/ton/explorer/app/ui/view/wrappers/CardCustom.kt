package ru.tetraquark.ton.explorer.app.ui.view.wrappers

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme

@Composable
fun CardCustom(
    modifier: Modifier = Modifier,
    shape: Shape = CustomTheme.shapes.cardShape,
    backgroundColor: Color = CustomTheme.colors.surface,
    contentColor: Color = CustomTheme.colors.text,
    border: BorderStroke? = null,
    elevation: Dp = 1.dp,
    content: @Composable () -> Unit
) {
    Card(modifier, shape, backgroundColor, contentColor, border, elevation, content)
}
