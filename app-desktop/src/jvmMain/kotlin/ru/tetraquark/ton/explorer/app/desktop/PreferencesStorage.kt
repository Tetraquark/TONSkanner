package ru.tetraquark.ton.explorer.app.desktop

import java.util.prefs.Preferences

object PreferencesStorage {
    val preferences = Preferences.userNodeForPackage(javaClass)
}
