package ru.tetraquark.ton.explorer.app.ui.features.component.queryhistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.view.wrappers.CardCustom
import ru.tetraquark.ton.explorer.features.component.queryhistory.QueryHistoryFeature

@Composable
fun QueryHistoryView(
    modifier: Modifier = Modifier,
    feature: QueryHistoryFeature,
) {
    CardCustom(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            feature.addressesHistory.forEachIndexed { index, address ->
                AddressHistoryColumnItem(
                    modifier = Modifier.fillMaxWidth(),
                    address = address,
                    onClick = { feature.onHistoryAddressClicked(index) }
                )
            }
        }
    }
}

@Composable
private fun AddressHistoryColumnItem(
    modifier: Modifier = Modifier,
    address: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CustomTheme.dimens.medium),
            text = address,
            color = CustomTheme.colors.text,
            style = CustomTheme.typography.normal14,
        )
    }
}
