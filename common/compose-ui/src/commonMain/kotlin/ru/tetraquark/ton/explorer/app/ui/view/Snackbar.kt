package ru.tetraquark.ton.explorer.app.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.utils.asString
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc
import ru.tetraquark.ton.explorer.features.root.MR

@Composable
fun SnackbarViewDismissable(
    modifier: Modifier = Modifier,
    message: String,
    dismissButtonText: String,
    onClose: () -> Unit = {}
) {
    Snackbar(
        modifier = modifier,
        backgroundColor = CustomTheme.colors.surface,
        action = {
            Text(
                modifier = Modifier
                    .padding(CustomTheme.dimens.xxSmall)
                    .clickable { onClose() },
                text = dismissButtonText,
                style = CustomTheme.typography.button,
                color = CustomTheme.colors.primary
            )
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = message,
                style = CustomTheme.typography.normal14,
                color = CustomTheme.colors.text
            )
        }
    }
}

@Composable
fun DefaultDismissableSnackbar(
    data: SnackbarData
) {
    SnackbarViewDismissable(
        modifier = Modifier.padding(horizontal = CustomTheme.dimens.medium),
        message = data.message,
        dismissButtonText = MR.strings.common_ok.asString(),
        onClose = { data.dismiss() }
    )
}

@Composable
fun rememberErrorSnackbar(
    errorEvent: ErrorDesc?,
    onErrorShown: () -> Unit
): SnackbarHostState {
    val snackbarHostState = remember { SnackbarHostState() }
    val callbackWrapper by rememberUpdatedState(newValue = onErrorShown)

    errorEvent?.let { event ->
        val messageText = event.message.asString()
        LaunchedEffect(messageText) {
            snackbarHostState.showSnackbar(
                message = messageText,
                duration = SnackbarDuration.Long
            )
            callbackWrapper()
        }
    }
    return snackbarHostState
}

