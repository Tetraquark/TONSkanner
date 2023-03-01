package ru.tetraquark.ton.explorer.app.ui.features.component.blockchain.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.utils.asString
import ru.tetraquark.ton.explorer.app.ui.view.ColumnSpacer
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.CoinAmount
import ru.tetraquark.ton.explorer.features.component.blockchain.resources.toStringDesc
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.MRTransactions
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model.entity.MessageAddress

@Composable
actual fun TransactionMessageAddressesView(
    fromAddress: MessageAddress,
    toAddress: MessageAddress,
    isOutDirection: Boolean,
    coinAmount: CoinAmount?,
    modifier: Modifier,
) {
    Column(
        modifier = modifier.padding(CustomTheme.dimens.small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            SelectionContainer {
                Text(
                    modifier = Modifier
                        .padding(horizontal = CustomTheme.dimens.xxSmall),
                    text = fromAddress.address.toStringDesc().asString(),
                    style = CustomTheme.typography.normal14,
                    color = if (fromAddress.isTargetAddress || fromAddress.isNoAddress) {
                        CustomTheme.colors.text
                    } else {
                        CustomTheme.colors.primary
                    },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val backgroundColor: Color
            val directionText: StringDesc
            if (isOutDirection) {
                backgroundColor = CustomTheme.colors.warning.copy(alpha = .5f)
                directionText = MRTransactions.strings.direction_out.desc()
            } else {
                backgroundColor = CustomTheme.colors.success.copy(alpha = .5f)
                directionText = MRTransactions.strings.direction_in.desc()
            }

            Text(
                modifier = Modifier
                    .padding(horizontal = CustomTheme.dimens.small)
                    .background(
                        color = backgroundColor,
                        shape = CustomTheme.shapes.cardShape
                    )
                    .padding(horizontal = CustomTheme.dimens.small),
                text = directionText.asString(),
                style = CustomTheme.typography.normal14,
                color = CustomTheme.colors.text,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            coinAmount?.toStringDesc()?.asString()?.let { amountText ->
                ColumnSpacer(CustomTheme.dimens.small)
                SelectionContainer {
                    Text(
                        text = amountText,
                        style = CustomTheme.typography.normal14,
                        color = CustomTheme.colors.text,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            SelectionContainer {
                Text(
                    modifier = Modifier
                        .padding(horizontal = CustomTheme.dimens.xxSmall),
                    text = toAddress.address.toStringDesc().asString(),
                    style = CustomTheme.typography.normal14,
                    color = if (toAddress.isTargetAddress || toAddress.isNoAddress) {
                        CustomTheme.colors.text
                    } else {
                        CustomTheme.colors.primary
                    },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}