package ru.tetraquark.ton.explorer.app.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme

@Composable
fun TopAppBarSimple(
    title: String,
    navigationIcons: @Composable (RowScope.() -> Unit)? = null,
    actionIcons: @Composable (RowScope.() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(16.dp)
    ) {
        navigationIcons?.let {
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .padding(start = 16.dp)
                    .align(Alignment.CenterStart),
                content = it
            )
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            color = CustomTheme.colors.text,
            style = CustomTheme.typography.medium20,
            textAlign = TextAlign.Center
        )
        actionIcons?.let {
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .padding(start = 16.dp)
                    .align(Alignment.CenterEnd),
                content = it
            )
        }
    }
}

@Composable
fun TopAppBarCustom(
    modifier: Modifier = Modifier,
    backgroundColor: Color = CustomTheme.colors.surface,
    contentColor: Color = CustomTheme.colors.onPrimary,
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    contentPadding: PaddingValues = AppBarDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    TopAppBar(modifier, backgroundColor, contentColor, elevation, contentPadding, content)
}

@Composable
fun TopAppBarCustom(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = CustomTheme.colors.surface,
    contentColor: Color = CustomTheme.colors.onPrimary,
    elevation: Dp = AppBarDefaults.TopAppBarElevation
) {
    TopAppBar(title, modifier, navigationIcon, actions, backgroundColor, contentColor, elevation)
}
