package ru.tetraquark.ton.explorer.app.ui.view.wrappers

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme

@Composable
fun OutlinedTextFieldCustom(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = CustomTheme.shapes.small,
    colors: TextFieldColors = outlinedTextFieldColorsDefault()
) {
    androidx.compose.material.OutlinedTextField(
        value,
        onValueChange,
        modifier,
        enabled,
        readOnly,
        textStyle,
        label,
        placeholder,
        leadingIcon,
        trailingIcon,
        isError,
        visualTransformation,
        keyboardOptions,
        keyboardActions,
        singleLine,
        maxLines,
        interactionSource,
        shape,
        colors
    )
}

@Composable
private fun outlinedTextFieldColorsDefault(): TextFieldColors = outlinedTextFieldColors(
    textColor = CustomTheme.colors.text,
    backgroundColor = CustomTheme.colors.surface,
    cursorColor = CustomTheme.colors.primary,
    errorCursorColor = CustomTheme.colors.error,
    focusedBorderColor = CustomTheme.colors.secondary,
    unfocusedBorderColor = CustomTheme.colors.outline,
    errorBorderColor = CustomTheme.colors.error,
    leadingIconColor = CustomTheme.colors.secondary,
    trailingIconColor = CustomTheme.colors.secondary,
    errorTrailingIconColor = CustomTheme.colors.error,
    focusedLabelColor = CustomTheme.colors.secondary,
    unfocusedLabelColor = CustomTheme.colors.secondary.copy(ContentAlpha.medium),
    placeholderColor = CustomTheme.colors.outline
)
