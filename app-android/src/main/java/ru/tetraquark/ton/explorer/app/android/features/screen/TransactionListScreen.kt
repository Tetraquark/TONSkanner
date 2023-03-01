package ru.tetraquark.ton.explorer.app.android.features.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.tetraquark.ton.explorer.app.ui.features.component.blockchain.transactions.TransactionsHistory
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.utils.asString
import ru.tetraquark.ton.explorer.app.ui.utils.subscribeAsState
import ru.tetraquark.ton.explorer.app.ui.utils.toPainter
import ru.tetraquark.ton.explorer.app.ui.view.DefaultDismissableSnackbar
import ru.tetraquark.ton.explorer.app.ui.view.TopAppBarCustom
import ru.tetraquark.ton.explorer.app.ui.view.rememberErrorSnackbar
import ru.tetraquark.ton.explorer.features.screen.transactionlist.MRTransactionList
import ru.tetraquark.ton.explorer.features.screen.transactionlist.TransactionListScreen

@Composable
fun TransactionListScreen(
    feature: TransactionListScreen
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
            .fillMaxSize(),
        scaffoldState = scaffoldState,
        backgroundColor = CustomTheme.colors.background,
        topBar = {
            TopAppBarCustom(
                modifier = Modifier,
                title = {
                    Text(
                        text = MRTransactionList.strings.transactionlist_title.asString(),
                        color = CustomTheme.colors.text,
                        style = CustomTheme.typography.medium20,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                indication = rememberRipple(bounded = false, radius = 14.dp),
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = feature::onBackButtonClicked
                            ),
                        painter = CustomTheme.drawables.arrowLeft.toPainter(),
                        tint = CustomTheme.colors.secondary,
                        contentDescription = "backbutton_icon"
                    )
                },
                backgroundColor = CustomTheme.colors.surface
            )
        },
        snackbarHost = { state ->
            SnackbarHost(
                hostState = state,
                snackbar = { DefaultDismissableSnackbar(it) }
            )
        }
    ) { scaffoldPaddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPaddings)
        ) {
            TransactionsHistory(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(CustomTheme.dimens.small),
                feature = feature.transactionsHistoryFeature
            )
        }
    }
}
