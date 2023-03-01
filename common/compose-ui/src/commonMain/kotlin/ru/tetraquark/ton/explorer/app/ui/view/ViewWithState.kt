package ru.tetraquark.ton.explorer.app.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.utils.asString
import ru.tetraquark.ton.explorer.app.ui.utils.toPainter
import ru.tetraquark.ton.explorer.app.ui.view.wrappers.TextButtonCustom
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc
import ru.tetraquark.ton.explorer.features.root.MR
import ru.tetraquark.ton.explorer.lib.state.Stateable

@Composable
fun <V, E> ViewWithState(
    state: Stateable<V, E>,
    failure: @Composable (Stateable.Failure<E>) -> Unit,
    loading: @Composable () -> Unit,
    success: @Composable (Stateable.Success<V>) -> Unit
) {
    when (state) {
        is Stateable.Failure -> failure(state)
        is Stateable.Loading -> loading()
        is Stateable.Success -> success(state)
    }
}

@Composable
fun <V> ViewWithState(
    state: Stateable<V, ErrorDesc>,
    failureModifier: Modifier,
    onFailureReloadClick: () -> Unit,
    loadingModifier: Modifier = Modifier,
    success: @Composable (Stateable.Success<V>) -> Unit
) {
    ViewWithState(
        state = state,
        failure = { ViewFailureStateDefault(failureModifier, it, onFailureReloadClick) },
        loading = { ViewLoadingStateDefault(loadingModifier) },
        success = { success(it) }
    )
}

@Composable
fun ViewLoadingStateDefault(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = CustomTheme.colors.primary,
            strokeWidth = 3.dp
        )
    }
}

@Composable
private fun ViewFailureStateDefault(
    modifier: Modifier = Modifier,
    failureState: Stateable.Failure<ErrorDesc>,
    onReloadClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(CustomTheme.dimens.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val failureImage = failureState.failure.icon?.toPainter()
            ?: CustomTheme.drawables.gmailErrorred.toPainter()

        Icon(
            modifier = Modifier.size(128.dp),
            painter = failureImage,
            contentDescription = "Failure icon",
            tint = CustomTheme.colors.secondary
        )

        ColumnSpacer(CustomTheme.dimens.small)

        // Title
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = CustomTheme.dimens.xxLarge),
            text = MR.strings.common_failure.asString(),
            style = CustomTheme.typography.semiBold16,
            color = CustomTheme.colors.text,
            textAlign = TextAlign.Center
        )

        ColumnSpacer(CustomTheme.dimens.small)

        // Message
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = CustomTheme.dimens.xxLarge),
            text = failureState.failure.message.asString(),
            style = CustomTheme.typography.normal14,
            color = CustomTheme.colors.text,
            textAlign = TextAlign.Center
        )

        ColumnSpacer(CustomTheme.dimens.medium)

        TextButtonCustom(
            modifier = Modifier
                .sizeIn(maxWidth = CustomTheme.dimens.maxButtonWidthDesktop)
                .fillMaxWidth(),
            text = MR.strings.common_reload.asString(),
            onClick = onReloadClick
        )
    }
}
