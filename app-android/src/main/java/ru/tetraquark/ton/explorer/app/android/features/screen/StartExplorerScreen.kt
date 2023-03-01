package ru.tetraquark.ton.explorer.app.android.features.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.tetraquark.ton.explorer.app.ui.features.component.inputaddress.InputAddressView
import ru.tetraquark.ton.explorer.app.ui.features.component.queryhistory.QueryHistoryView
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.utils.asString
import ru.tetraquark.ton.explorer.app.ui.utils.subscribeAsState
import ru.tetraquark.ton.explorer.app.ui.view.ColumnSpacer
import ru.tetraquark.ton.explorer.app.ui.view.DefaultDismissableSnackbar
import ru.tetraquark.ton.explorer.app.ui.view.rememberErrorSnackbar
import ru.tetraquark.ton.explorer.features.screen.startexplorer.MRStartExplorer
import ru.tetraquark.ton.explorer.features.screen.startexplorer.StartExplorerScreen

@Composable
fun StartExplorerScreen(
    feature: StartExplorerScreen
) {
    val screenState by feature.screenState.subscribeAsState()

    val scaffoldState = rememberScaffoldState(
        snackbarHostState = rememberErrorSnackbar(
            errorEvent = screenState.errorEvent,
            onErrorShown = feature::onErrorShown
        )
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(CustomTheme.dimens.medium),
        scaffoldState = scaffoldState,
        backgroundColor = CustomTheme.colors.background,
        snackbarHost = { state ->
            SnackbarHost(
                hostState = state,
                snackbar = { DefaultDismissableSnackbar(it) }
            )
        }
    ) { scaffoldPaddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPaddings),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputAddressView(
                modifier = Modifier.fillMaxWidth(),
                feature = feature.inputAddressFeature
            )

            if (feature.queryHistoryFeature.addressesHistory.isNotEmpty()) {
                ColumnSpacer(CustomTheme.dimens.medium)
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = MRStartExplorer.strings.history_title.asString(),
                    color = CustomTheme.colors.text,
                    style = CustomTheme.typography.medium16,
                )
                ColumnSpacer(CustomTheme.dimens.medium)
                QueryHistoryView(
                    modifier = Modifier.fillMaxWidth()
                        .background(
                            color = CustomTheme.colors.surface,
                            shape = CustomTheme.shapes.cardShape
                        ),
                    feature = feature.queryHistoryFeature
                )
            }
        }
    }
}
