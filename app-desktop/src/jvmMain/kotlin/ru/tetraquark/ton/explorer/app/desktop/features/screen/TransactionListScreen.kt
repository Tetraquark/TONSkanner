package ru.tetraquark.ton.explorer.app.desktop.features.screen

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ru.tetraquark.ton.explorer.app.ui.features.component.blockchain.transactions.TransactionsHistory
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.utils.asString
import ru.tetraquark.ton.explorer.app.ui.utils.toPainter
import ru.tetraquark.ton.explorer.app.ui.view.ScreenSurfaceWithSnackbar
import ru.tetraquark.ton.explorer.app.ui.view.TopAppBarSimple
import ru.tetraquark.ton.explorer.app.ui.view.rememberErrorSnackbar
import ru.tetraquark.ton.explorer.features.screen.transactionlist.MRTransactionList
import ru.tetraquark.ton.explorer.features.screen.transactionlist.TransactionListScreen

@Composable
fun TransactionListScreen(
    feature: TransactionListScreen
) {
    val screenState by feature.screenState.subscribeAsState()

    val errorSnackbarHostState = rememberErrorSnackbar(
        errorEvent = screenState.errorEvent,
        onErrorShown = feature::onErrorShown
    )

    ScreenSurfaceWithSnackbar(
        snackbarHostState = errorSnackbarHostState,
        topAppBar = {
            TopAppBarSimple(
                title = MRTransactionList.strings.transactionlist_title.asString(),
                navigationIcons = {
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
                }
            )
        }
    ) {
        TransactionsHistory(
            feature = feature.transactionsHistoryFeature,
            modifier = Modifier.fillMaxSize(),
            successStateContent = {
                VerticalScrollbar(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(
                        scrollState = it
                    )
                )
            }
        )
    }
}
