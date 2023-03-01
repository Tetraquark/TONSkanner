package ru.tetraquark.ton.explorer.app.desktop

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.russhwolf.settings.PreferencesSettings
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.component.setupKtorComponents
import dev.icerock.moko.resources.desc.image.ImageDesc
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import ru.tetraquark.ton.explorer.app.desktop.features.screen.ExplorerMainScreen
import ru.tetraquark.ton.explorer.app.desktop.features.screen.StartExplorerScreen
import ru.tetraquark.ton.explorer.app.desktop.features.screen.TransactionListScreen
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.theme.CustomThemeDrawables
import ru.tetraquark.ton.explorer.app.ui.utils.Drawable
import ru.tetraquark.ton.explorer.app.ui.utils.asString
import ru.tetraquark.ton.explorer.core.ton.entity.TonConfig
import ru.tetraquark.ton.explorer.features.base.theme.ThemeMode
import ru.tetraquark.ton.explorer.features.root.AppConfig
import ru.tetraquark.ton.explorer.features.root.MR
import ru.tetraquark.ton.explorer.features.root.RootComponent
import ru.tetraquark.ton.explorer.features.root.RootFeature

@OptIn(ExperimentalDecomposeApi::class)
internal fun startComposeApplication() {
    val appLifecycle = LifecycleRegistry()

    val rootComponent = RootComponent(
        componentContext = DefaultComponentContext(appLifecycle),
        httpClientEngine = buildOkHttpClientEngine(),
        settings = PreferencesSettings(PreferencesStorage.preferences),
        tonConfig = TonConfig.Url("https://ton.org/global-config.json"),
        appConfig = AppConfig(
            tonDnsRootCollectionAddress = "EQC3dNlesgVD8YbAazcauIrXBPfiVhMMr5YYk2in0Mtsz0Bz"
        ),
    )

    val imageLoader = buildImageLoader(rootComponent.httpClient)

    application {
        val windowState = rememberWindowState(
            size = DpSize(1024.dp, 768.dp)
        )
        LifecycleController(appLifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = MR.strings.app_name.asString()
        ) {
            MenuBar {
                Menu(text = "View") {
                    Item(
                        text = MR.strings.common_change_theme.asString(),
                        onClick = rootComponent::onChangeThemeClicked
                    )
                }
            }
            CompositionLocalProvider(
                LocalImageLoader provides imageLoader
            ) {
                val themeMode by rootComponent.themeMode.subscribeAsState()
                val isDarkTheme = when (themeMode) {
                    ThemeMode.SYSTEM -> isSystemInDarkTheme()
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                }
                CustomTheme(
                    isDarkTheme = isDarkTheme,
                    drawables = buildCustomThemeDrawables()
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = CustomTheme.colors.background
                    ) {
                        RootView(
                            rootFeature = rootComponent,
                        )
                    }
                }
            }
        }
    }
}

private fun buildCustomThemeDrawables(): CustomThemeDrawables = CustomThemeDrawables(
    arrowUp = ImageDesc.Drawable("ic_arrow_up"),
    arrowDown = ImageDesc.Drawable("ic_arrow_down"),
    arrowLeft = ImageDesc.Drawable("ic_arrow_left"),
    arrowRight = ImageDesc.Drawable("ic_arrow_right"),
    keyboardArrowRight = ImageDesc.Drawable("ic_keyboard_arrow_right"),
    search = ImageDesc.Drawable("ic_search"),
    copy = ImageDesc.Drawable("ic_copy"),
    gmailErrorred = ImageDesc.Drawable("ic_report_gmailerrorred"),
    sentimentVeryDissatisfied = ImageDesc.Drawable("ic_sentiment_very_dissatisfied"),
    list = ImageDesc.Drawable("ic_list"),
    image = ImageDesc.Drawable("ic_image"),
    tokens = ImageDesc.Drawable("ic_tokens")
) 

private fun buildImageLoader(
    httpClient: HttpClient
): ImageLoader {
    return ImageLoader {
        components {
            setupKtorComponents { httpClient }
        }
    }
}

private fun buildOkHttpClientEngine(): HttpClientEngine {
    return OkHttpEngine(
        OkHttpConfig().apply {
            // TODO
        }
    )
}

@Composable
fun RootView(
    rootFeature: RootFeature,
) {
    val childStack by rootFeature.childStack.subscribeAsState()

    Children(childStack) {
        when (val child = it.instance) {
            is RootFeature.Child.StartExplorer -> {
                StartExplorerScreen(child.screen)
            }
            is RootFeature.Child.ExplorerMain -> {
                ExplorerMainScreen(child.screen)
            }
            is RootFeature.Child.TransactionList -> {
                TransactionListScreen(child.screen)
            }
        }
    }
}
