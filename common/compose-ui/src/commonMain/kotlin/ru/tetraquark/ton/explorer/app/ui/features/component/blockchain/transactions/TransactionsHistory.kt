package ru.tetraquark.ton.explorer.app.ui.features.component.blockchain.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.utils.asString
import ru.tetraquark.ton.explorer.app.ui.utils.subscribeAsState
import ru.tetraquark.ton.explorer.app.ui.view.ColumnSpacer
import ru.tetraquark.ton.explorer.app.ui.view.DividerDashed
import ru.tetraquark.ton.explorer.app.ui.view.ViewWithState
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.CoinAmount
import ru.tetraquark.ton.explorer.features.component.blockchain.resources.toStringDesc
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.MRTransactions
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.TransactionsHistoryFeature
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model.entity.MessageAddress
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model.entity.TransactionListItem
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model.entity.TransactionMessage

@Composable
fun TransactionsHistory(
    feature: TransactionsHistoryFeature,
    modifier: Modifier = Modifier,
    successStateContent: @Composable BoxScope.(LazyListState) -> Unit = {},
) {
    val state by feature.state.subscribeAsState()

    Box(
        modifier = modifier
    ) {
        ViewWithState(
            state = state,
            failureModifier = Modifier.fillMaxWidth().align(Alignment.Center),
            loadingModifier = Modifier.fillMaxWidth().align(Alignment.Center),
            onFailureReloadClick = feature::reloadData,
        ) { successState ->
            if (successState.data.itemList.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(CustomTheme.dimens.medium)
                        .align(Alignment.Center),
                    text = MRTransactions.strings.transactions_not_found.asString(),
                    style = CustomTheme.typography.normal16,
                    color = CustomTheme.colors.secondary,
                )
            } else {
                val lazyColumnState = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = CustomTheme.dimens.medium,
                            end = CustomTheme.dimens.medium,
                            bottom = CustomTheme.dimens.medium,
                        ),
                    state = lazyColumnState,
                    verticalArrangement = Arrangement.spacedBy(CustomTheme.dimens.medium),
                ) {
                    itemsIndexed(
                        items = successState.data.itemList,
                        key = { _, item -> item.id },
                    ) { index, item ->
                        TransactionListItemView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 3.dp,
                                    shape = CustomTheme.shapes.cardShape,
                                ),
                            data = item,
                            onItemClick = { feature.onItemClicked(item) }
                        )

                        if (index == successState.data.itemList.lastIndex - 1) {
                            LaunchedEffect(Unit) {
                                feature.loadNextPage()
                            }
                        }
                    }
                    if (successState.data.isPageLoading) {
                        item {
                            LoadingColumnItem()
                        }
                    }
                }
                successStateContent(lazyColumnState)
            }
        }
    }
}

@Composable
private fun LoadingColumnItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(CustomTheme.dimens.xLarge),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = CustomTheme.colors.primary,
            strokeWidth = 3.dp
        )
    }
}

@Composable
private fun TransactionListItemView(
    data: TransactionListItem,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = CustomTheme.colors.surface,
                shape = CustomTheme.shapes.cardShape
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = CustomTheme.colors.secondary.copy(alpha = .1f)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SelectionContainer {
                val dateText = remember { data.formatDate() }
                Text(
                    modifier = Modifier.padding(CustomTheme.dimens.medium),
                    text = dateText,
                    style = CustomTheme.typography.normal14,
                    color = CustomTheme.colors.secondary,
                )
            }

            Text(
                modifier = Modifier.padding(horizontal = CustomTheme.dimens.xxSmall),
                text = MRTransactions.strings.total_fee_title.asString(),
                style = CustomTheme.typography.normal14,
                color = CustomTheme.colors.secondary,
            )
            SelectionContainer {
                val coinsText = remember { data.totalFee.toStringDesc() }
                Text(
                    modifier = Modifier.padding(horizontal = CustomTheme.dimens.xxSmall),
                    text = coinsText.asString(),
                    style = CustomTheme.typography.normal14,
                    color = CustomTheme.colors.secondary,
                )
            }
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = CustomTheme.colors.outline
        )


        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            data.inMessage?.let { msg ->
                TonTransactionMsgView(
                    modifier = Modifier.fillMaxWidth(),
                    data = msg,
                    isOutDirection = false
                )

                if (data.outMessages.isNotEmpty()) {
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = CustomTheme.colors.outline
                    )
                }
            }
            data.outMessages.forEachIndexed { index, msg ->
                TonTransactionMsgView(
                    modifier = Modifier.fillMaxWidth(),
                    data = msg,
                    isOutDirection = true
                )
                if (index != data.outMessages.lastIndex) {
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = CustomTheme.colors.outline
                    )
                }
            }
        }
    }
}

@Composable
private fun TonTransactionMsgView(
    data: TransactionMessage,
    isOutDirection: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        when (data) {
            is TransactionMessage.IntMsgInfo -> {
                TransactionMessageAddressesView(
                    modifier = Modifier.fillMaxWidth(),
                    fromAddress = data.fromAddress,
                    toAddress = data.toAddress,
                    isOutDirection = isOutDirection,
                    coinAmount = data.tonAmount
                )
            }
            else -> {
                TransactionMessageAddressesView(
                    modifier = Modifier.fillMaxWidth(),
                    fromAddress = data.fromAddress,
                    toAddress = data.toAddress,
                    isOutDirection = isOutDirection
                )
            }
        }
        data.textComment?.let { comment ->
            if (comment.isNotEmpty()) {
                DividerDashed(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = CustomTheme.dimens.small),
                    strokeWidth = 2.dp
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = CustomTheme.dimens.medium,
                            top = CustomTheme.dimens.medium
                        ),
                    text = MRTransactions.strings.comment_title.asString(),
                    style = CustomTheme.typography.normal14,
                    color = CustomTheme.colors.secondary,
                )
                SelectionContainer {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(CustomTheme.dimens.medium),
                        text = comment,
                        style = CustomTheme.typography.normal14,
                        color = CustomTheme.colors.text,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3
                    )
                }
            }
        }
    }
}
