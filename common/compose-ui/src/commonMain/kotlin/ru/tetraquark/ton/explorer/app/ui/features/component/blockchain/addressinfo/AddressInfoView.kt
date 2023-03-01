package ru.tetraquark.ton.explorer.app.ui.features.component.blockchain.addressinfo

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.desc.image.ImageDesc
import dev.icerock.moko.resources.desc.image.ImageDescUrl
import kotlinx.coroutines.launch
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.utils.asString
import ru.tetraquark.ton.explorer.app.ui.utils.subscribeAsState
import ru.tetraquark.ton.explorer.app.ui.utils.toPainter
import ru.tetraquark.ton.explorer.app.ui.view.ColumnSpacer
import ru.tetraquark.ton.explorer.app.ui.view.RowSpacer
import ru.tetraquark.ton.explorer.app.ui.view.ViewWithState
import ru.tetraquark.ton.explorer.app.ui.view.wrappers.CardCustom
import ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo.AddressInfoFeature
import ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo.MRAddressInfo
import ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo.getTypeText
import ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo.model.entity.AddressInfo
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.Address
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.MRBlockchain
import ru.tetraquark.ton.explorer.features.component.blockchain.resources.toStringDesc
import ru.tetraquark.ton.explorer.features.root.MR

@Composable
fun AddressInfoView(
    modifier: Modifier = Modifier,
    feature: AddressInfoFeature,
    copyToClipboard: (String) -> Unit,
    openInWebBrowser: suspend (String) -> Unit,
) {
    val state by feature.state.subscribeAsState()

    Box(
        modifier = modifier
            .heightIn(min = 120.dp)
            .animateContentSize()
    ) {
        ViewWithState(
            state = state,
            failureModifier = Modifier.fillMaxWidth().align(Alignment.Center),
            loadingModifier = Modifier.fillMaxWidth().align(Alignment.Center),
            onFailureReloadClick = feature::reloadData,
        ) { successState ->
            val addressInfo = successState.data.addressInfo

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (addressInfo == null) {
                    AddressNotFound()
                } else {
                    AddressInfoCard(
                        modifier = Modifier.fillMaxWidth(),
                        addressInfo = addressInfo,
                        onAddressFieldClick = feature::onAddressFieldClicked,
                        copyToClipboard = copyToClipboard
                    )

                    if (addressInfo !is AddressInfo.WalletContract &&
                        addressInfo !is AddressInfo.UnknownContract)
                    {
                        var isShowingMoreDetails by remember { mutableStateOf(false) }

                        ColumnSpacer(CustomTheme.dimens.medium)
                        MoreDetailsButton(
                            isShowingMoreDetails = isShowingMoreDetails,
                            onClick = { isShowingMoreDetails = !isShowingMoreDetails }
                        )
                        ColumnSpacer(CustomTheme.dimens.medium)

                        if (isShowingMoreDetails) {
                            val coroutineScope = rememberCoroutineScope()
                            DetailedAddressInfoContent(
                                modifier = Modifier.fillMaxWidth(),
                                addressInfo = addressInfo,
                                onAddressFieldClick = feature::onAddressFieldClicked,
                                copyToClipboard = copyToClipboard,
                                tryOpenImageInBrowser = { img ->
                                    if (img is ImageDescUrl) {
                                        coroutineScope.launch { openInWebBrowser(img.url) }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddressInfoCard(
    addressInfo: AddressInfo,
    onAddressFieldClick: (Address) -> Unit,
    copyToClipboard: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    CardCustom(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = CustomTheme.dimens.medium,
                    vertical = CustomTheme.dimens.small
                )
        ) {
            TitleWithTextAddressView(
                title = MRAddressInfo.strings.address_title.asString(),
                addressText = addressInfo.address.toStringDesc().asString(),
                onTextClick = { onAddressFieldClick(addressInfo.address) },
                copyToClipboard = copyToClipboard,
            )
            ColumnSpacer(CustomTheme.dimens.medium)
            TitleWithTextView(
                title = MRAddressInfo.strings.balance_title.asString(),
                value = addressInfo.balance.toStringDesc().asString()
            )
            ColumnSpacer(CustomTheme.dimens.medium)
            TitleWithTextView(
                title = MRAddressInfo.strings.state_title.asString(),
                value = addressInfo.state.toStringDesc().asString()
            )
            ColumnSpacer(CustomTheme.dimens.medium)
            TitleWithTextView(
                title = MRAddressInfo.strings.contract_type_title.asString(),
                value = addressInfo.getTypeText().asString()
            )
        }
    }
}

@Composable
private fun DetailedAddressInfoContent(
    addressInfo: AddressInfo,
    onAddressFieldClick: (Address) -> Unit,
    copyToClipboard: (String) -> Unit,
    tryOpenImageInBrowser: (ImageDesc?) -> Unit,
    modifier: Modifier = Modifier,
) {
    CardCustom(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = CustomTheme.dimens.medium,
                    vertical = CustomTheme.dimens.small
                )
        ) {
            when (addressInfo) {
                is AddressInfo.JettonWalletContract -> {
                    JettonWalletContractDetailedView(
                        info = addressInfo,
                        onAddressFieldClick = onAddressFieldClick,
                        copyToClipboard = copyToClipboard,
                        tryOpenImageInBrowser = tryOpenImageInBrowser
                    )
                }
                is AddressInfo.JettonContract -> {
                    JettonContractDetailedView(
                        info = addressInfo,
                        onAddressFieldClick = onAddressFieldClick,
                        copyToClipboard = copyToClipboard,
                        tryOpenImageInBrowser = tryOpenImageInBrowser
                    )
                }
                is AddressInfo.NftCollectionContract -> {
                    NftCollectionDetailedView(
                        info = addressInfo,
                        onAddressFieldClick = onAddressFieldClick,
                        copyToClipboard = copyToClipboard,
                        tryOpenImageInBrowser = tryOpenImageInBrowser
                    )
                }
                is AddressInfo.NftItemContract -> {
                    NftItemDetailedView(
                        info = addressInfo,
                        onAddressFieldClick = onAddressFieldClick,
                        copyToClipboard = copyToClipboard,
                        tryOpenImageInBrowser = tryOpenImageInBrowser
                    )
                }
                is AddressInfo.NftDnsCollectionContract -> {
                    NftDnsCollectionDetailedView(addressInfo)
                }
                is AddressInfo.NftDnsItemContract -> {
                    NftDnsItemDetailedView(
                        info = addressInfo,
                        onAddressFieldClick = onAddressFieldClick,
                        copyToClipboard = copyToClipboard,
                    )
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun ColumnScope.NftCollectionDetailedView(
    info: AddressInfo.NftCollectionContract,
    onAddressFieldClick: (Address) -> Unit,
    copyToClipboard: (String) -> Unit,
    tryOpenImageInBrowser: (ImageDesc?) -> Unit
) {
    TitleWithTextView(
        title = MRAddressInfo.strings.name_title.asString(),
        value = info.name
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    info.descriptionText?.let { text ->
        TitleWithTextView(
            title = MRAddressInfo.strings.description_title.asString(),
            value = text
        )
        ColumnSpacer(CustomTheme.dimens.medium)
    }
    info.marketplace?.let { text ->
        TitleWithTextView(
            title = MRAddressInfo.strings.marketplace_title.asString(),
            value = text
        )
        ColumnSpacer(CustomTheme.dimens.medium)
    }
    TitleWithTextView(
        title = MRAddressInfo.strings.nftcount_title.asString(),
        value = info.nftItemsCount.toString()
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    TitleWithTextAddressView(
        title = MRAddressInfo.strings.owner_address_title.asString(),
        addressText = info.ownerAddress.toStringDesc().asString(),
        onTextClick = { onAddressFieldClick(info.ownerAddress) },
        copyToClipboard = copyToClipboard,
    )
    info.image?.toPainter()?.let { painter ->
        ColumnSpacer(CustomTheme.dimens.medium)
        ImageBox(
            painter = painter,
            onClick = { tryOpenImageInBrowser(info.image) }
        )
        ColumnSpacer(CustomTheme.dimens.medium)
    }
}

@Composable
private fun ColumnScope.NftDnsCollectionDetailedView(
    info: AddressInfo.NftDnsCollectionContract
) {
    TitleWithTextView(
        title = MRAddressInfo.strings.name_title.asString(),
        value = info.name
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    info.descriptionText?.let { text ->
        TitleWithTextView(
            title = MRAddressInfo.strings.description_title.asString(),
            value = text
        )
        ColumnSpacer(CustomTheme.dimens.medium)
    }
}

@Composable
private fun ColumnScope.NftItemDetailedView(
    info: AddressInfo.NftItemContract,
    onAddressFieldClick: (Address) -> Unit,
    copyToClipboard: (String) -> Unit,
    tryOpenImageInBrowser: (ImageDesc?) -> Unit
) {
    TitleWithTextView(
        title = MRAddressInfo.strings.name_title.asString(),
        value = info.name
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    info.descriptionText?.let { text ->
        TitleWithTextView(
            title = MRAddressInfo.strings.description_title.asString(),
            value = text
        )
        ColumnSpacer(CustomTheme.dimens.medium)
    }
    TitleWithTextView(
        title = MRAddressInfo.strings.initialized_title.asString(),
        value = info.init.toString()
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    TitleWithTextAddressView(
        title = MRAddressInfo.strings.collection_address_title.asString(),
        addressText = info.collectionAddress.toStringDesc().asString(),
        onTextClick = { onAddressFieldClick(info.collectionAddress) },
        copyToClipboard = copyToClipboard,
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    info.ownerAddress?.let { addr ->
        TitleWithTextAddressView(
            title = MRAddressInfo.strings.owner_address_title.asString(),
            addressText = addr.toStringDesc().asString(),
            onTextClick = { onAddressFieldClick(addr) },
            copyToClipboard = copyToClipboard,
        )
    } ?: TitleWithTextView(
        title = MRAddressInfo.strings.owner_address_title.asString(),
        value = MRBlockchain.strings.noaddress.asString()
    )

    ColumnSpacer(CustomTheme.dimens.medium)
    TitleWithTextView(
        title = MRAddressInfo.strings.nftindex_title.asString(),
        value = info.index.toString()
    )
    info.image?.toPainter()?.let { painter ->
        ColumnSpacer(CustomTheme.dimens.medium)
        ImageBox(
            painter = painter,
            onClick = { tryOpenImageInBrowser(info.image) }
        )
        ColumnSpacer(CustomTheme.dimens.medium)
    }
}

@Composable
private fun ColumnScope.NftDnsItemDetailedView(
    info: AddressInfo.NftDnsItemContract,
    onAddressFieldClick: (Address) -> Unit,
    copyToClipboard: (String) -> Unit,
) {
    TitleWithTextView(
        title = MRAddressInfo.strings.name_title.asString(),
        value = info.dnsName + ".ton"
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    TitleWithTextView(
        title = MRAddressInfo.strings.initialized_title.asString(),
        value = info.init.toString()
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    TitleWithTextAddressView(
        title = MRAddressInfo.strings.collection_address_title.asString(),
        addressText = info.collectionAddress.toStringDesc().asString(),
        onTextClick = { onAddressFieldClick(info.collectionAddress) },
        copyToClipboard = copyToClipboard,
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    info.ownerAddress?.let { addr ->
        TitleWithTextAddressView(
            title = MRAddressInfo.strings.owner_address_title.asString(),
            addressText = addr.toStringDesc().asString(),
            onTextClick = { onAddressFieldClick(addr) },
            copyToClipboard = copyToClipboard,
        )
    } ?: TitleWithTextView(
        title = MRAddressInfo.strings.owner_address_title.asString(),
        value = MRBlockchain.strings.noaddress.asString()
    )
}

@Composable
private fun ColumnScope.JettonWalletContractDetailedView(
    info: AddressInfo.JettonWalletContract,
    onAddressFieldClick: (Address) -> Unit,
    copyToClipboard: (String) -> Unit,
    tryOpenImageInBrowser: (ImageDesc?) -> Unit
) {
    TitleWithTextView(
        title = MRAddressInfo.strings.balance_title.asString(),
        value = info.walletBalance.toStringDesc().asString()
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    TitleWithTextAddressView(
        title = MRAddressInfo.strings.root_jetton_address_title.asString(),
        addressText = info.rootAddress.toStringDesc().asString(),
        onTextClick = { onAddressFieldClick(info.rootAddress) },
        copyToClipboard = copyToClipboard,
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    TitleWithTextAddressView(
        title = MRAddressInfo.strings.owner_address_title.asString(),
        addressText = info.ownerAddress.toStringDesc().asString(),
        onTextClick = { onAddressFieldClick(info.ownerAddress) },
        copyToClipboard = copyToClipboard,
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    info.logoImage?.toPainter()?.let { painter ->
        ImageBox(
            painter = painter,
            onClick = { tryOpenImageInBrowser(info.logoImage) }
        )
        ColumnSpacer(CustomTheme.dimens.medium)
    }
}

@Composable
private fun ColumnScope.JettonContractDetailedView(
    info: AddressInfo.JettonContract,
    onAddressFieldClick: (Address) -> Unit,
    copyToClipboard: (String) -> Unit,
    tryOpenImageInBrowser: (ImageDesc?) -> Unit
) {
    TitleWithTextView(
        title = MRAddressInfo.strings.name_title.asString(),
        value = info.name
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    TitleWithTextView(
        title = MRAddressInfo.strings.symbol_title.asString(),
        value = info.symbol
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    info.descriptionText?.let { text ->
        TitleWithTextView(
            title = MRAddressInfo.strings.description_title.asString(),
            value = text
        )
        ColumnSpacer(CustomTheme.dimens.medium)
    }
    TitleWithTextView(
        title = MRAddressInfo.strings.ismintable_title.asString(),
        value = info.isMintable.toString()
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    TitleWithTextView(
        title = MRAddressInfo.strings.total_supply_title.asString(),
        value = info.totalSupplyText
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    TitleWithTextAddressView(
        title = MRAddressInfo.strings.admin_address_title.asString(),
        addressText = info.adminAddress.toStringDesc().asString(),
        onTextClick = { onAddressFieldClick(info.adminAddress) },
        copyToClipboard = copyToClipboard,
    )
    info.logoImage?.toPainter()?.let { painter ->
        ColumnSpacer(CustomTheme.dimens.medium)
        ImageBox(
            painter = painter,
            onClick = { tryOpenImageInBrowser(info.logoImage) }
        )
        ColumnSpacer(CustomTheme.dimens.medium)
    }
}

@Composable
private fun ImageBox(
    painter: Painter,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            modifier = Modifier
                .size(256.dp)
                .clip(CustomTheme.shapes.cardShape)
                .align(Alignment.Center)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClick
                ),
            painter = painter,
            contentDescription = "contract_image"
        )
    }
}

@Composable
private fun MoreDetailsButton(
    isShowingMoreDetails: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClick
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val text = if (isShowingMoreDetails) {
                MRAddressInfo.strings.less_details
            } else {
                MRAddressInfo.strings.more_details
            }.asString()

            Text(
                modifier = Modifier.padding(horizontal = CustomTheme.dimens.xxSmall),
                text = text,
                style = CustomTheme.typography.normal14,
                color = CustomTheme.colors.primary,
            )

            val painter = if (isShowingMoreDetails) {
                CustomTheme.drawables.arrowUp.toPainter()
            } else {
                CustomTheme.drawables.arrowDown.toPainter()
            }
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painter,
                tint = CustomTheme.colors.primary,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ColumnScope.AddressNotFound() {
    Icon(
        modifier = Modifier.size(64.dp),
        painter = CustomTheme.drawables.sentimentVeryDissatisfied.toPainter(),
        tint = CustomTheme.colors.primary.copy(alpha = 0.5f),
        contentDescription = null
    )
    ColumnSpacer(CustomTheme.dimens.medium)
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = CustomTheme.dimens.medium),
        text = MR.strings.common_nothing_found.asString(),
        style = CustomTheme.typography.normal14,
        color = CustomTheme.colors.text,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun TextWithCopyView(
    text: String,
    onTextClick: () -> Unit,
    onCopyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onTextClick
            ),
            text = text,
            style = CustomTheme.typography.medium16,
            color = CustomTheme.colors.primary,
        )
        RowSpacer(CustomTheme.dimens.small)
        Icon(
            modifier = Modifier
                .size(16.dp)
                .clickable(
                    indication = rememberRipple(bounded = false, radius = 9.dp),
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onCopyClick
                ),
            painter = CustomTheme.drawables.copy.toPainter(),
            tint = CustomTheme.colors.secondary,
            contentDescription = null
        )
    }

}

@Composable
private fun TitleWithTextView(
    title: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier,
            text = title,
            style = CustomTheme.typography.normal14,
            color = CustomTheme.colors.secondary,
            maxLines = 1,
        )
        ColumnSpacer(CustomTheme.dimens.small)
        SelectionContainer {
            Text(
                modifier = Modifier,
                text = value.takeIf { it.isNotBlank() } ?: "---",
                style = CustomTheme.typography.medium16,
                color = CustomTheme.colors.text,
            )
        }
    }
}

@Composable
private fun TitleWithTextAddressView(
    title: String,
    addressText: String,
    onTextClick: () -> Unit,
    copyToClipboard: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier,
            text = title,
            style = CustomTheme.typography.normal14,
            color = CustomTheme.colors.secondary,
            maxLines = 1,
        )
        ColumnSpacer(CustomTheme.dimens.small)
        TextWithCopyView(
            modifier = Modifier.fillMaxWidth(),
            text = addressText,
            onTextClick = onTextClick,
            onCopyClick = { copyToClipboard(addressText) }
        )
    }
}
