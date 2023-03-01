package ru.tetraquark.ton.explorer.app.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.icerock.moko.resources.desc.desc
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.utils.asString
import ru.tetraquark.ton.explorer.features.root.MR

@Composable
fun ScreenSurface(
    topAppBar: @Composable (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = CustomTheme.colors.background),
    ) {
        topAppBar?.invoke()
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            content()
        }
    }
}

@Composable
fun ScreenSurfaceWithSnackbar(
    snackbarHostState: SnackbarHostState,
    snackbarModifier: Modifier? = null,
    snackbarContent: @Composable (SnackbarData) -> Unit = { DefaultDismissableSnackbar(it) },
    topAppBar: @Composable (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    ScreenSurface(
        topAppBar = topAppBar
    ) {
        content()

        SnackbarHost(
            modifier = snackbarModifier ?: Modifier.align(Alignment.TopCenter),
            hostState = snackbarHostState,
            snackbar = snackbarContent
        )
    }
}
