package ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo

import com.arkivanov.decompose.value.Value
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc
import ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo.model.entity.AddressInfo
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.Address
import ru.tetraquark.ton.explorer.lib.state.Stateable

interface AddressInfoFeature {
    val state: Value<Stateable<Model, ErrorDesc>>

    fun loadData(address: String)

    fun reloadData()

    fun onAddressFieldClicked(address: Address)

    data class Model(
        val addressInfo: AddressInfo?
    )
}
