package com.jeovanimartinez.androidutils.share

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.analytics.Event
import java.util.*

/**
 * Auxiliary BroadcastReceiver that is invoked when sharing content with ShareUtils, the broadcast is invoked only
 * if the user chose an app, if the chooser is canceled, the broadcast is never invoked.
 * */
internal class ApplicationSelectorReceiver : BroadcastReceiver() {

    /**
     * It is received when the user selects an app to share the content.
     * */
    override fun onReceive(context: Context, intent: Intent) {
        ShareUtils.log("Invoked > ApplicationSelectorReceiver > onReceive")
        ShareUtils.firebaseAnalytics(Event.SHARE_COMPLETED, Bundle().apply {
            putString(Event.Parameter.SHARE_CASE, intent.extras!!.getString("case"))
            putString(Event.Parameter.SHARE_SELECTED_APP, getSelectedAppName(context, intent))
        })
    }

    // Reference: https://stackoverflow.com/a/50288268
    /**
     * Gets the name of the selected application when sharing.
     * */
    @SuppressLint("Assert")
    private fun getSelectedAppName(context: Context, intent: Intent): String {
        var appName = Event.ParameterValue.N_A
        try {
            @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
            for (key in Objects.requireNonNull(intent.extras)!!.keySet()) {
                try {
                    val componentInfo = intent.extras!![key] as ComponentName?
                    val packageManager = context.packageManager
                    assert(componentInfo != null)
                    appName = packageManager.getApplicationLabel(packageManager.getApplicationInfo(componentInfo!!.packageName, PackageManager.GET_META_DATA)) as String
                } catch (e: Exception) {
                    // It is not necessary to register, it occurs if the information of the intent extra cannot be obtained since it is of another type
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Base.firebaseCrashlyticsInstance?.recordException(e)
        }
        if (appName.length > 100) appName = appName.subSequence(0, 100).toString() // It is reduced to a maximum of 100 characters to be able to register the event in firebase analytics
        return appName
    }

}
