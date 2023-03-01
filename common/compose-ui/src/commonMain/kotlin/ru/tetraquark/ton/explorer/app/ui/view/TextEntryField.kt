package ru.tetraquark.ton.explorer.app.ui.view

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import dev.icerock.moko.resources.desc.StringDesc
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.utils.asString
import ru.tetraquark.ton.explorer.app.ui.view.wrappers.OutlinedTextFieldCustom
import ru.tetraquark.ton.explorer.lib.entryfield.TextEntryField

@Composable
fun OutlineTextEntryField(
    field: TextEntryField<StringDesc>,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = CustomTheme.shapes.small,
) {
    val valueState by field.valueState.collectAsState()
    val errorState by field.errorState.collectAsState()
    val isEnabledState by field.isEnabled.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        OutlinedTextFieldCustom(
            value = valueState,
            onValueChange = field::setValue,
            modifier = Modifier.fillMaxWidth(),
            enabled = isEnabledState,
            readOnly = readOnly,
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = errorState != null,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape
        )
        ColumnSpacer(CustomTheme.dimens.small)
        errorState?.let {
            FieldErrorText(it.asString())
        } ?: FieldErrorText(" ")
    }
}

@Composable
fun OutlineTextEntryField(
    field: TextEntryField<StringDesc>,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    labelText: String? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = CustomTheme.shapes.small,
) {
    OutlineTextEntryField(
        field = field,
        modifier = modifier,
        readOnly = readOnly,
        textStyle = textStyle,
        label = labelText?.let {
            { Text(labelText) }
        },
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        shape = shape
    )
}

@Composable
private fun FieldErrorText(text: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        style = CustomTheme.typography.normal14,
        color = CustomTheme.colors.error,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}
