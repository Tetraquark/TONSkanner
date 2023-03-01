package ru.tetraquark.ton.explorer.app.ui.features.screen.explorermain

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.utils.asString
import ru.tetraquark.ton.explorer.app.ui.utils.toPainter
import ru.tetraquark.ton.explorer.app.ui.view.wrappers.CardCustom
import ru.tetraquark.ton.explorer.features.screen.explorermain.ExplorerMainScreen
import ru.tetraquark.ton.explorer.features.screen.explorermain.MRExplorerMain

@Composable
fun ExplorerMainNavigationCard(feature: ExplorerMainScreen) {
    CardCustom(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val dividerColor = CustomTheme.colors.outline
            val transactionsText = MRExplorerMain.strings.transaction_history_button.asString()
            NavigationRowButton(
                modifier = Modifier.fillMaxWidth(),
                text = transactionsText,
                iconPainter = CustomTheme.drawables.list.toPainter(),
                onClick = feature::onTransactionListClicked,
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp),
                color = dividerColor
            )

            val nftListText = MRExplorerMain.strings.nft_list_button.asString()
            NavigationRowButton(
                modifier = Modifier.fillMaxWidth(),
                text = nftListText,
                iconPainter = CustomTheme.drawables.image.toPainter(),
                onClick = {},
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp),
                color = dividerColor
            )

            val tokensText = MRExplorerMain.strings.jettons_button.asString()
            NavigationRowButton(
                modifier = Modifier.fillMaxWidth(),
                text = tokensText,
                iconPainter = CustomTheme.drawables.tokens.toPainter(),
                onClick = {},
            )
        }
    }
}

@Composable
fun NavigationRowButton(
    text: String,
    iconPainter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick),
    ) {
        Icon(
            modifier = Modifier
                .padding(start = CustomTheme.dimens.medium)
                .size(24.dp)
                .align(Alignment.CenterStart),
            painter = iconPainter,
            tint = CustomTheme.colors.secondary,
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 48.dp,
                    vertical = CustomTheme.dimens.medium
                )
                .align(Alignment.CenterStart),
            text = text,
            style = CustomTheme.typography.medium16,
            color = CustomTheme.colors.text,
            maxLines = 1,
        )
        Icon(
            modifier = Modifier
                .padding(end = CustomTheme.dimens.medium)
                .size(24.dp)
                .align(Alignment.CenterEnd),
            painter = CustomTheme.drawables.keyboardArrowRight.toPainter(),
            tint = CustomTheme.colors.secondary,
            contentDescription = null
        )
    }
}
