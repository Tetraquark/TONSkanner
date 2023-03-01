package ru.tetraquark.ton.explorer.app.desktop.features.screen

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ru.tetraquark.ton.explorer.app.desktop.utils.copyToClipboard
import ru.tetraquark.ton.explorer.app.desktop.utils.openInDefaultWebBrowser
import ru.tetraquark.ton.explorer.app.ui.features.component.blockchain.addressinfo.AddressInfoView
import ru.tetraquark.ton.explorer.app.ui.features.component.inputaddress.InputAddressView
import ru.tetraquark.ton.explorer.app.ui.features.screen.explorermain.ExplorerMainNavigationCard
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.view.ColumnSpacer
import ru.tetraquark.ton.explorer.app.ui.view.ScreenSurfaceWithSnackbar
import ru.tetraquark.ton.explorer.app.ui.view.rememberErrorSnackbar
import ru.tetraquark.ton.explorer.features.screen.explorermain.ExplorerMainScreen
import ru.tetraquark.ton.explorer.lib.state.Stateable

@Composable
fun ExplorerMainScreen(
    feature: ExplorerMainScreen
) {
    val screenState by feature.screenState.subscribeAsState()
    val addressInfoState by feature.addressInfoFeature.state.subscribeAsState()

    val errorSnackbarHostState = rememberErrorSnackbar(
        errorEvent = screenState.errorEvent,
        onErrorShown = feature::onErrorShown
    )

    ScreenSurfaceWithSnackbar(
        snackbarHostState = errorSnackbarHostState,
    ) {
        val verticalScrollState = rememberScrollState()
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(CustomTheme.dimens.medium)
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
                    copyToClipboard = ::copyToClipboard,
                    openInWebBrowser = { openInDefaultWebBrowser(it) }
                )

                ColumnSpacer(CustomTheme.dimens.large)

                if (addressInfoState is Stateable.Success) {
                    ExplorerMainNavigationCard(feature)
                }
            }

            VerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                adapter = rememberScrollbarAdapter(verticalScrollState)
            )
        }
    }
}
