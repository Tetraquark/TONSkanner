package ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.tetraquark.ton.explorer.features.base.resources.ErrorDesc
import ru.tetraquark.ton.explorer.features.base.resources.ExceptionMapper
import ru.tetraquark.ton.explorer.features.base.viewmodel.ViewModelBase
import ru.tetraquark.ton.explorer.features.component.blockchain.addressinfo.model.AddressInfoRepository
import ru.tetraquark.ton.explorer.lib.state.Stateable

internal class AddressInfoViewModel(
    private val addressInfoRepository: AddressInfoRepository,
    private val exceptionMapper: ExceptionMapper
) : ViewModelBase() {
    val state: MutableStateFlow<Stateable<AddressInfoFeature.Model, ErrorDesc>> =
        MutableStateFlow(Stateable.Loading)

    var address: String? = null

    private var loadingJob: Job? = null

    fun loadAddressInfo(address: String) {
        loadingJob?.cancel()
        state.value = Stateable.Loading
        this.address = address
        loadingJob = instanceScope.launch {
            state.value = try {
                val info = addressInfoRepository.getAddressInfo(address)
                Stateable.Success(
                    AddressInfoFeature.Model(info)
                )
            } catch (ex: Exception) {
                Stateable.Failure(exceptionMapper(ex))
            } finally {
                loadingJob = null
            }
        }
    }
}
