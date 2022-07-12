package com.jeovanimartinez.androidutils.app

import android.util.Log
import androidx.multidex.MultiDexApplication

/** App Singleton */
class App : MultiDexApplication() {

    @Suppress("PrivatePropertyName")
    private val LOG_TAG = "AppSingleton"

    override fun onCreate() {
        super.onCreate()

        Log.d(LOG_TAG, "Started Android Utils App")
    }

}
