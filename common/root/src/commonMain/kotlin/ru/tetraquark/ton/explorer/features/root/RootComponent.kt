package ru.tetraquark.ton.explorer.features.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import ru.tetraquark.ton.explorer.core.storage.SettingsStorage
import ru.tetraquark.ton.explorer.core.ton.TonLiteClient
import ru.tetraquark.ton.explorer.core.ton.TonLiteClientApi
import ru.tetraquark.ton.explorer.core.ton.entity.TonAddress
import ru.tetraquark.ton.explorer.core.ton.entity.TonConfig
import ru.tetraquark.ton.explorer.core.ton.service.TonConfigLoader
import ru.tetraquark.ton.explorer.core.ton.service.TonDnsResolver
import ru.tetraquark.ton.explorer.core.ton.service.TonHttpJsonOffchainLoader
import ru.tetraquark.ton.explorer.features.base.decompose.asValue
import ru.tetraquark.ton.explorer.features.base.theme.ThemeMode
import ru.tetraquark.ton.explorer.features.component.blockchain.transactions.TransactionsHistoryComponent
import ru.tetraquark.ton.explorer.features.root.model.repository.ExplorerHistoryRepository
import ru.tetraquark.ton.explorer.features.root.validation.validateTONWalletAddressField
import ru.tetraquark.ton.explorer.features.screen.explorermain.ExplorerMainScreenComponent
import ru.tetraquark.ton.explorer.features.screen.startexplorer.StartExplorerScreenComponent
import ru.tetraquark.ton.explorer.features.screen.transactionlist.TransactionListScreenComponent

class RootComponent(
    componentContext: ComponentContext,
    settings: Settings,
    tonConfig: TonConfig,
    private val appConfig: AppConfig,
    httpClientEngine: HttpClientEngine?,
    deepLink: DeepLink = DeepLink.None
) : RootFeature, ComponentContext by componentContext {

    private val json: Json by lazy {
        Json {
            ignoreUnknownKeys = true
        }
    }

    private val settingsStorage: SettingsStorage by lazy {
        SettingsStorage(settings, json)
    }

    private val tonLiteClientApi: TonLiteClientApi by lazy {
        TonLiteClient(
            configLoader = TonConfigLoader(
                tonConfig = tonConfig,
                json = json,
                httpClient = httpClient,
            ),
            offchainDataLoader = TonHttpJsonOffchainLoader(
                json = json,
                httpClient = httpClient,
            ),
            tonDnsResolver = TonDnsResolver(
                tonDnsRootCollectionAddress = TonAddress.BasicMasterchain(
                    appConfig.tonDnsRootCollectionAddress
                )
            )
        )
    }

    val httpClient: HttpClient by lazy {
        buildKtorHttpClient(
            json = json,
            httpClientEngine = httpClientEngine
        )
    }

    private val navigation = StackNavigation<Config>()

    private val stack = childStack(
        source = navigation,
        initialStack = { getInitialStack(deepLink) },
        childFactory = ::childFactory
    )

    override val childStack: Value<ChildStack<*, RootFeature.Child>>
        get() = stack

    private val themeModeState = MutableValue(getThemeModeFromStorage())
    override val themeMode: Value<ThemeMode> get() = themeModeState

    constructor(
        componentContext: ComponentContext,
        settings: Settings,
        tonConfig: TonConfig,
        appConfig: AppConfig,
        deepLink: DeepLink = DeepLink.None
    ) : this(componentContext, settings, tonConfig, appConfig, null, deepLink)

    override fun onChangeThemeClicked() {
        settingsStorage.isDarkModeTheme = settingsStorage.isDarkModeTheme?.let { !it } ?: true
        themeModeState.value = getThemeModeFromStorage()
    }

    private fun getThemeModeFromStorage(): ThemeMode {
        return when {
            settingsStorage.isDarkModeTheme == false -> ThemeMode.LIGHT
            settingsStorage.isDarkModeTheme == true -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
    }

    private fun childFactory(
        config: Config,
        componentContext: ComponentContext
    ): RootFeature.Child {
        return when (config) {
            Config.StartExplorerScreen -> RootFeature.Child.StartExplorer(
                StartExplorerScreenComponent(
                    componentContext = componentContext,
                    storage = settingsStorage,
                    exceptionMapper = ExceptionMapperImpl,
                    initTonLiteClient = {
                        if (!tonLiteClientApi.isInitialized) {
                            tonLiteClientApi.init()
                        }
                    },
                    routeNextScreen = {
                        navigation.replaceCurrent(
                            configuration = Config.ExplorerMainScreen(it)
                        )
                    },
                    walletAddressValidationRule = ::validateTONWalletAddressField,
                    explorerHistoryStorageMaxSize = EXPLORER_HISTORY_STORAGE_SIZE
                )
            )
            is Config.ExplorerMainScreen -> RootFeature.Child.ExplorerMain(
                ExplorerMainScreenComponent(
                    componentContext = componentContext,
                    storage = settingsStorage,
                    tonLiteClientApi = tonLiteClientApi,
                    exceptionMapper = ExceptionMapperImpl,
                    walletAddressValidationRule = ::validateTONWalletAddressField,
                    initAddress = config.address,
                    explorerHistoryStorageMaxSize = EXPLORER_HISTORY_STORAGE_SIZE,
                    routeToTransactionList = {
                        navigation.push(
                            configuration = Config.TransactionListScreen(it)
                        )
                    }
                )
            )
            is Config.TransactionListScreen -> RootFeature.Child.TransactionList(
                TransactionListScreenComponent(
                    componentContext = componentContext,
                    tonLiteClientApi = tonLiteClientApi,
                    exceptionMapper = ExceptionMapperImpl,
                    address = config.address,
                    routeBack = { navigation.pop() }
                )
            )
        }
    }

    private companion object {
        private const val EXPLORER_HISTORY_STORAGE_SIZE = 10
        private fun getInitialStack(deepLink: DeepLink): List<Config> {
            return when (deepLink) {
                DeepLink.None -> listOf(Config.StartExplorerScreen)
            }
        }
    }

    private sealed interface Config : Parcelable {
        @Parcelize
        object StartExplorerScreen : Config

        @Parcelize
        data class ExplorerMainScreen(val address: String) : Config

        @Parcelize
        data class TransactionListScreen(val address: String) : Config
    }

    sealed interface DeepLink {
        object None : DeepLink
    }
}
