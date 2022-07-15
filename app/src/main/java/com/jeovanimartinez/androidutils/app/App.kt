@file:Suppress("UnusedImport")

package com.jeovanimartinez.androidutils.app

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.app.constants.Preferences
import com.google.firebase.analytics.FirebaseAnalytics

/** App Singleton */
class App : MultiDexApplication() {

    @Suppress("PrivatePropertyName")
    private val LOG_TAG = "AppSingleton"

    override fun onCreate() {
        super.onCreate()

        Log.d(LOG_TAG, "Started Android Utils App")

        themeSetup()

        // Tests, to appreciate the splash screen
        // for (i in 1..100000) {
        //    Log.d(LOG_TAG, i.toString())
        // }


        Base.logEnable = BuildConfig.DEBUG // Adjust the debug log

        /**
         * Assigning the Firebase Analytics instance to test event logging.
         *
         * Enable Analytics debug mode: adb shell setprop debug.firebase.analytics.app com.jeovanimartinez.androidutils.app
         * Disable Analytics debug mode: adb shell setprop debug.firebase.analytics.app .none.
         * */
        // Base.firebaseAnalyticsInstance = FirebaseAnalytics.getInstance(this)

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
