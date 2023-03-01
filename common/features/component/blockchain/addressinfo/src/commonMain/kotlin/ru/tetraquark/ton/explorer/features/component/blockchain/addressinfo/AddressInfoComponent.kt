package ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.tetraquark.ton.explorer.core.ton.TonLiteClientApi
import ru.tetraquark.ton.explorer.features.base.decompose.asValue
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc
import ru.tetraquark.ton.explorer.features.base.resources.ExceptionMapper
import ru.tetraquark.ton.explorer.features.base.viewmodel.ViewModelBase
import ru.tetraquark.ton.explorer.features.base.viewmodel.viewModel
import ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo.model.AddressInfoRepository
import ru.tetraquark.ton.explorer.features.component.blockchain.entity.Address
import ru.tetraquark.ton.explorer.lib.state.Stateable
import ru.tetraquark.ton.explorer.lib.state.getIfSuccess

class AddressInfoComponent(
    componentContext: ComponentContext,
    tonLiteClientApi: TonLiteClientApi,
    private val loadNewAddress: (String) -> Unit,
    private val exceptionMapper: ExceptionMapper
) : AddressInfoFeature, ComponentContext by componentContext {

    private val repository = AddressInfoRepository(tonLiteClientApi)

    private val viewModel = viewModel(instanceKeeper) {
        AddressInfoViewModel(repository, exceptionMapper)
    }

    override val state: Value<Stateable<AddressInfoFeature.Model, ErrorDesc>> =
        viewModel.state.asValue(viewModel.instanceScope)

    override fun loadData(address: String) {
        viewModel.loadAddressInfo(address)
    }

    override fun reloadData() {
        viewModel.address?.run(viewModel::loadAddressInfo)
    }

    override fun onAddressFieldClicked(address: Address) {
        val addressInfo = viewModel.state.value.getIfSuccess()?.data?.addressInfo
        if (address is Address.SimpleAddress && addressInfo?.address != address) {
            loadNewAddress(address.address)
        }
    }
}
