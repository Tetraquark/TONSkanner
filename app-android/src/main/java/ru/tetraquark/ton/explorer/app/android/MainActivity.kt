package ru.tetraquark.ton.explorer.app.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.russhwolf.settings.SharedPreferencesSettings
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.resources.desc.image.ImageDesc
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import ru.tetraquark.ton.explorer.app.android.features.screen.ExplorerMainScreen
import ru.tetraquark.ton.explorer.app.android.features.screen.StartExplorerScreen
import ru.tetraquark.ton.explorer.app.android.features.screen.TransactionListScreen
import ru.tetraquark.ton.explorer.app.ui.theme.CustomTheme
import ru.tetraquark.ton.explorer.app.ui.theme.CustomThemeDrawables
import ru.tetraquark.ton.explorer.app.ui.utils.Drawable
import ru.tetraquark.ton.explorer.core.ton.entity.TonConfig
import ru.tetraquark.ton.explorer.features.base.theme.ThemeMode
import ru.tetraquark.ton.explorer.features.root.AppConfig
import ru.tetraquark.ton.explorer.features.root.MR
import ru.tetraquark.ton.explorer.features.root.RootComponent
import ru.tetraquark.ton.explorer.features.root.RootFeature

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootComponent = RootComponent(
            componentContext = defaultComponentContext(),
            httpClientEngine = buildOkHttpClientEngine(),
            settings = SharedPreferencesSettings(
                delegate = getSharedPreferences(MR.strings.app_name.desc().toString(this), MODE_PRIVATE)
            ),
            tonConfig = TonConfig.Url("https://ton.org/global-config.json"),
            appConfig = AppConfig(
                tonDnsRootCollectionAddress = "EQC3dNlesgVD8YbAazcauIrXBPfiVhMMr5YYk2in0Mtsz0Bz"
            ),
        )

        setContent {
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
                    val childStack by rootComponent.childStack.subscribeAsState()

                    Children(childStack) {
                        when (val child = it.instance) {
                            is RootFeature.Child.StartExplorer -> {
                                StartExplorerScreen(child.screen)
                            }
                            is RootFeature.Child.ExplorerMain -> {
                                ExplorerMainScreen(
                                    feature = child.screen,
                                    startActivity = ::startActivity
                                )
                            }
                            is RootFeature.Child.TransactionList -> {
                                TransactionListScreen(child.screen)
                            }
                        }
                    }
                }
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

    private fun buildCustomThemeDrawables(): CustomThemeDrawables = CustomThemeDrawables(
        arrowUp = ImageDesc.Drawable(R.drawable.ic_arrow_up),
        arrowDown = ImageDesc.Drawable(R.drawable.ic_arrow_down),
        arrowLeft = ImageDesc.Drawable(R.drawable.ic_arrow_left),
        arrowRight = ImageDesc.Drawable(R.drawable.ic_arrow_right),
        keyboardArrowRight = ImageDesc.Drawable(R.drawable.ic_keyboard_arrow_right),
        search = ImageDesc.Drawable(R.drawable.ic_search),
        copy = ImageDesc.Drawable(R.drawable.ic_copy),
        gmailErrorred = ImageDesc.Drawable(R.drawable.ic_report_gmailerrorred),
        sentimentVeryDissatisfied = ImageDesc.Drawable(R.drawable.ic_sentiment_very_dissatisfied),
        list = ImageDesc.Drawable(R.drawable.ic_list),
        image = ImageDesc.Drawable(R.drawable.ic_image),
        tokens = ImageDesc.Drawable(R.drawable.ic_tokens)
    )
}
