@file:OptIn(ExperimentalMaterialApi::class)

package ru.tetraquark.ton.explorer.app.ui.view.wrappers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme

@Composable
fun ButtonCustom(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = CustomTheme.shapes.actionButton,
    border: BorderStroke? = null,
    colors: ButtonColors = DefaultButtonCustomColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    val contentColor by colors.contentColor(enabled)
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = colors.backgroundColor(enabled).value,
        contentColor = contentColor.copy(alpha = 1f),
        border = border,
        elevation = elevation?.elevation(enabled, interactionSource)?.value ?: 0.dp,
        interactionSource = interactionSource,
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
            ProvideTextStyle(
                value = CustomTheme.typography.button
            ) {
                Row(
                    Modifier
                        .defaultMinSize(
                            minWidth = ButtonDefaults.MinWidth,
                            minHeight = ButtonDefaults.MinHeight
                        )
                        .padding(contentPadding),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

@Composable
fun OutlinedButtonCustom(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = null,
    shape: Shape = CustomTheme.shapes.actionButton,
    border: BorderStroke? = null,
    colors: ButtonColors = DefaultOutlinedButtonCustomColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    val _border = if (border == null && enabled) {
        BorderStroke(2.dp, CustomTheme.colors.primary)
    } else {
        null
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = _border,
        colors = colors,
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
fun TextButtonCustom(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = CustomTheme.shapes.actionButton,
    border: BorderStroke? = null,
    colors: ButtonColors = DefaultButtonCustomColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
    ) {
        Text(
            text = text
        )
    }
}

@Immutable
private class DefaultButtonCustomColors : ButtonColors {
    @Composable
    override fun backgroundColor(enabled: Boolean): State<Color> {
        val color = if (enabled) CustomTheme.colors.primary else CustomTheme.colors.outline
        return rememberUpdatedState(color)
    }

    @Composable
    override fun contentColor(enabled: Boolean): State<Color> {
        val color = if (enabled) CustomTheme.colors.surface else CustomTheme.colors.secondary
        return rememberUpdatedState(color)
    }
}

@Immutable
private class DefaultOutlinedButtonCustomColors : ButtonColors {
    @Composable
    override fun backgroundColor(enabled: Boolean): State<Color> {
        val color = if (enabled) CustomTheme.colors.surface else CustomTheme.colors.outline
        return rememberUpdatedState(color)
    }

    @Composable
    override fun contentColor(enabled: Boolean): State<Color> {
        val color = if (enabled) CustomTheme.colors.primary else CustomTheme.colors.secondary
        return rememberUpdatedState(color)
    }
}
