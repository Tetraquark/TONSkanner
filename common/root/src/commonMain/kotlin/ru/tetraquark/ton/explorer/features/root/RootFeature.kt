package ru.tetraquark.ton.explorer.features.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.tetraquark.ton.explorer.features.base.theme.ThemeMode
import ru.tetraquark.ton.explorer.features.screen.explorermain.ExplorerMainScreen
import ru.tetraquark.ton.explorer.features.screen.startexplorer.StartExplorerScreen
import ru.tetraquark.ton.explorer.features.screen.transactionlist.TransactionListScreen

interface RootFeature {

    val childStack: Value<ChildStack<*, Child>>

    val themeMode: Value<ThemeMode>

    fun onChangeThemeClicked()

    sealed class Child {
        data class StartExplorer(val screen: StartExplorerScreen) : Child()
        data class ExplorerMain(val screen: ExplorerMainScreen) : Child()
        data class TransactionList(val screen: TransactionListScreen) : Child()
    }
}
