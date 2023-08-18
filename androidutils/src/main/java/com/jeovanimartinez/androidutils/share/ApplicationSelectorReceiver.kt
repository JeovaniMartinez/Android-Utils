package com.jeovanimartinez.androidutils.share

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.analytics.Event
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull
import java.util.*

/**
 * Auxiliary BroadcastReceiver that is invoked when sharing content with ShareUtils, the broadcast is invoked only
 * if the user chose an app, if the chooser is canceled, the broadcast is never invoked.
 * */
internal class ApplicationSelectorReceiver : BroadcastReceiver() {

    companion object {
        // Auxiliary to determine the case for which the BroadcastReceiver is executed
        var currentShareCase = Event.ParameterValue.N_A
    }

    /**
     * It is received when the user selects an app to share the content.
     * */
    override fun onReceive(context: Context, intent: Intent) {
        ShareUtils.log("Invoked > ApplicationSelectorReceiver > onReceive")

        val selectedAppName = getSelectedAppName(context, intent)
        ShareUtils.log("Selected App Name: $selectedAppName")
        ShareUtils.logAnalyticsEvent(Event.SHARE_UTILS_SHARE_COMPLETED, Bundle().apply {
            putString(Event.Parameter.SHARE_UTILS_SHARE_CASE, currentShareCase)
            putString(Event.Parameter.SHARE_UTILS_SHARE_SELECTED_APP, selectedAppName)
        })
    }

    // Reference: https://stackoverflow.com/a/50288268
    /**
     * Gets the name of the selected application when sharing.
     * */
    private fun getSelectedAppName(context: Context, intent: Intent): String {

        var result = Event.ParameterValue.N_A // Default value

        try {
            intent.extras.whenNotNull { intentExtras ->
                // All keys of the bundle are iterated
                for (key in intentExtras.keySet()) {

                    // Depending on the Android version, and if the data type is ComponentName, it is assigned to the variable; otherwise, the variable is assigned to null
                    val componentInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intentExtras.getParcelable(key, ComponentName::class.java) // It is only assigned if it belongs to the ComponentName::class.java
                    } else {
                        @Suppress("DEPRECATION")
                        intentExtras.get(key) as? ComponentName // It is only assigned if it can be converted to ComponentName
                    }

                    componentInfo.whenNotNull {
                        // The app information is obtained
                        val appInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            context.packageManager.getApplicationInfo(it.packageName, PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong()))
                        } else {
                            @Suppress("DEPRECATION")
                            context.packageManager.getApplicationInfo(it.packageName, PackageManager.GET_META_DATA)
                        }
                        result = context.packageManager.getApplicationLabel(appInfo).toString() // Finally, the app name is obtained
                    }
                }
            }
        } catch (e: Exception) {
            // ** Ideally, an exception should never occur with the way this code is written; however, it is used only for safety **
            e.printStackTrace()
            Base.firebaseCrashlyticsInstance?.recordException(e)
        }

        // The result is reduced to a maximum of 100 characters to be able to register the event in firebase analytics
        if (result.length > 100) {
            result = result.subSequence(0, 100).toString()
        }

        return result
    }

}
