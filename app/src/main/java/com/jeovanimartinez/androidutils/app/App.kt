package com.jeovanimartinez.androidutils.app

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.app.constants.Preferences
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jeovanimartinez.androidutils.reviews.RateApp

/** App Singleton */
class App : MultiDexApplication() {

    @Suppress("PrivatePropertyName")
    private val LOG_TAG = "AppSingleton"

    override fun onCreate() {
        super.onCreate()

        Log.d(LOG_TAG, "Started Android Utils App")

        themeSetup()
        rateInAppSetup()

        // Tests, to appreciate the splash screen
        // for (i in 1..100000) {
        //    Log.d(LOG_TAG, i.toString())
        // }

        com.jeovanimartinez.androidutils.logutils.Log.logEnable = BuildConfig.DEBUG // Adjust the debug log

        Base.firebaseCrashlyticsInstance = FirebaseCrashlytics.getInstance()

        /**
         * Assigning the Firebase Analytics instance to test event logging.
         *
         * Enable Analytics debug mode: adb shell setprop debug.firebase.analytics.app com.jeovanimartinez.androidutils.app
         * Disable Analytics debug mode: adb shell setprop debug.firebase.analytics.app .none.
         * */
        Base.firebaseAnalyticsInstance = FirebaseAnalytics.getInstance(this)

    }

    /** Theme Setup */
    private fun themeSetup() {
        // The preferences theme is loaded and configured
        if (!getSharedPreferences(Preferences.GENERAL_PREFERENCES_FILE, Context.MODE_PRIVATE).getBoolean(Preferences.DARK_THEME_ENABLED, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    /** Rate In App Setup */
    private fun rateInAppSetup() {

        val preferences = getSharedPreferences(Preferences.RATE_APP_PREFERENCES_FILE, Context.MODE_PRIVATE)

        RateApp.apply {
            minInstallElapsedDays = preferences.getInt(Preferences.RATE_APP_MIN_INSTALL_ELAPSED_DAYS, 0)
            minInstallLaunchTimes = preferences.getInt(Preferences.RATE_APP_MIN_INSTALL_LAUNCH_TIMES, 1)
            minRemindElapsedDays = preferences.getInt(Preferences.RATE_APP_MIN_REMIND_ELAPSED_DAYS, 0)
            minRemindLaunchTimes = preferences.getInt(Preferences.RATE_APP_MIN_REMIND_LAUNCH_TIMES, 1)
            showAtEvent = preferences.getInt(Preferences.RATE_APP_SHOW_AT_EVENT, 1)
        }.init(this)
    }

}
