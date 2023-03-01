package ru.tetraquark.ton.explorer.app.desktop.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.net.URI
import java.util.Locale

fun copyToClipboard(text: String) {
    Toolkit.getDefaultToolkit().systemClipboard?.setContents(
        StringSelection(text),
        null
    )
}

suspend fun openInDefaultWebBrowser(url: String) {
    withContext(Dispatchers.Default) {
        val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
        val desktop = Desktop.getDesktop()
        when {
            Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE) -> {
                desktop.browse(URI.create(url))
            }
            "mac" in osName -> {
                Runtime.getRuntime().exec("open $url")
            }
            "nix" in osName || "nux" in osName -> {
                Runtime.getRuntime().exec("xdg-open $url")
            }
            else -> {}
        }
    }
}
