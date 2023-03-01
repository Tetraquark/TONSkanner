package ru.tetraquark.ton.explorer.app.ui.features.component.blockchain.transactions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.CoinAmount
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.model.entity.MessageAddress

@Composable
expect fun TransactionMessageAddressesView(
    fromAddress: MessageAddress,
    toAddress: MessageAddress,
    isOutDirection: Boolean,
    coinAmount: CoinAmount? = null,
    modifier: Modifier = Modifier,
)
