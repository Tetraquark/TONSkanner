@file:OptIn(ExperimentalComposeUiApi::class)

package ru.tetraquark.ton.explorer.app.ui.features.component.inputaddress

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.utils.asString
import ru.tetraquark.ton.explorer.app.ui.utils.subscribeAsState
import ru.tetraquark.ton.explorer.app.ui.utils.toPainter
import ru.tetraquark.ton.explorer.app.ui.view.OutlineTextEntryField
import ru.tetraquark.ton.explorer.features.component.inputaddress.InputAddressFeature
import ru.tetraquark.ton.explorer.features.component.inputaddress.MRInputAddress

@Composable
fun InputAddressView(
    modifier: Modifier = Modifier,
    feature: InputAddressFeature
) {
    val isLoading by feature.isExplorerLoading.subscribeAsState()
    Box(
        modifier = modifier
    ) {
        OutlineTextEntryField(
            modifier = Modifier
                .fillMaxWidth()
                .onKeyEvent {
                    if (it.key == Key.Enter) {
                        feature.onContinueButtonClicked()
                        true
                    } else {
                        false
                    }
                },
            field = feature.addressField,
            labelText = MRInputAddress.strings.input_address_field_label.asString(),
            trailingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = CustomTheme.colors.primary,
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(
                        modifier = Modifier.padding(end = CustomTheme.dimens.xxSmall),
                        onClick = feature::onContinueButtonClicked,
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = CustomTheme.drawables.search.toPainter(),
                            contentDescription = null,
                            tint = CustomTheme.colors.primary
                        )
                    }
                }
            },
            maxLines = 1
        )
    }
}
