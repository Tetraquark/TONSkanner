package ru.tetraquark.ton.explorer.app.android.features.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import ru.tetraquark.ton.explorer.app.ui.features.component.blockchain.addressinfo.AddressInfoView
import ru.tetraquark.ton.explorer.app.ui.features.component.inputaddress.InputAddressView
import ru.tetraquark.ton.explorer.app.ui.features.screen.explorermain.ExplorerMainNavigationCard
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.utils.subscribeAsState
import ru.tetraquark.ton.explorer.app.ui.view.ColumnSpacer
import ru.tetraquark.ton.explorer.app.ui.view.DefaultDismissableSnackbar
import ru.tetraquark.ton.explorer.app.ui.view.rememberErrorSnackbar
import ru.tetraquark.ton.explorer.features.screen.explorermain.ExplorerMainScreen
import ru.tetraquark.ton.explorer.lib.state.Stateable

@Composable
fun ExplorerMainScreen(
    feature: ExplorerMainScreen,
    startActivity: (Intent) -> Unit
) {
    val screenState by feature.screenState.subscribeAsState()
    val addressInfoState by feature.addressInfoFeature.state.subscribeAsState()

    val clipBoardManager = LocalClipboardManager.current
    val startActivityWrapped by rememberUpdatedState(startActivity)
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
        val verticalScrollState = rememberScrollState()
        Box(
            modifier = Modifier.fillMaxSize().padding(scaffoldPaddings),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(verticalScrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                InputAddressView(
                    modifier = Modifier.fillMaxWidth(),
                    feature = feature.inputAddressFeature
                )

                ColumnSpacer(CustomTheme.dimens.xxSmall)

                AddressInfoView(
                    modifier = Modifier.fillMaxWidth(),
                    feature = feature.addressInfoFeature,
                    copyToClipboard = { clipBoardManager.setText(AnnotatedString(it)) },
                    openInWebBrowser = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                        startActivityWrapped(intent)
                    }
                )

                ColumnSpacer(CustomTheme.dimens.large)

                if (addressInfoState is Stateable.Success) {
                    ExplorerMainNavigationCard(feature)
                }
            }
        }
    }
}
