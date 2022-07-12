package com.jeovanimartinez.androidutils.app

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.jeovanimartinez.androidutils.app.constants.Preferences

/** App Singleton */
class App : MultiDexApplication() {

    @Suppress("PrivatePropertyName")
    private val LOG_TAG = "AppSingleton"

    override fun onCreate() {
        super.onCreate()

        Log.d(LOG_TAG, "Started Android Utils App")

        themeSetup()
    }

    /** Theme Setup */
    private fun themeSetup() {
        // The preferences theme is loaded and configured
        if (!getSharedPreferences(Preferences.THEME_PREFERENCES_FILE, Context.MODE_PRIVATE).getBoolean(Preferences.THEME_DARK_THEME_ENABLED, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

}
