package ru.tetraquark.ton.explorer.app.desktop.features.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ru.tetraquark.ton.explorer.app.ui.features.component.inputaddress.InputAddressView
import ru.tetraquark.ton.explorer.app.ui.features.component.queryhistory.QueryHistoryView
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.utils.asString
import ru.tetraquark.ton.explorer.app.ui.view.ColumnSpacer
import ru.tetraquark.ton.explorer.app.ui.view.ScreenSurfaceWithSnackbar
import ru.tetraquark.ton.explorer.app.ui.view.rememberErrorSnackbar
import ru.tetraquark.ton.explorer.features.screen.startexplorer.MRStartExplorer
import ru.tetraquark.ton.explorer.features.screen.startexplorer.StartExplorerScreen

@Composable
fun StartExplorerScreen(
    feature: StartExplorerScreen
) {
    val screenState by feature.screenState.subscribeAsState()

    val errorSnackbarHostState = rememberErrorSnackbar(
        errorEvent = screenState.errorEvent,
        onErrorShown = feature::onErrorShown
    )

    ScreenSurfaceWithSnackbar(
        snackbarHostState = errorSnackbarHostState,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputAddressView(
                modifier = Modifier.fillMaxWidth().padding(CustomTheme.dimens.medium),
                feature = feature.inputAddressFeature
            )

            if (feature.queryHistoryFeature.addressesHistory.isNotEmpty()) {
                ColumnSpacer(CustomTheme.dimens.medium)
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = CustomTheme.dimens.medium),
                    text = MRStartExplorer.strings.history_title.asString(),
                    color = CustomTheme.colors.text,
                    style = CustomTheme.typography.medium16,
                )
                ColumnSpacer(CustomTheme.dimens.medium)
                QueryHistoryView(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = CustomTheme.dimens.medium)
                        .background(
                            color = CustomTheme.colors.surface,
                            shape = CustomTheme.shapes.cardShape
                        )
                        .clipToBounds(),
                    feature = feature.queryHistoryFeature
                )
            }
        }
    }
}
