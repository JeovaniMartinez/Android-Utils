package com.jeovanimartinez.androidutils.web

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.URLUtil
import androidx.annotation.Size
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.analytics.Event
import com.jeovanimartinez.androidutils.extensions.context.shortToast
import java.lang.Exception

/**
 * Utility to interact with the system web browser.
 * */
object SystemWebBrowser : Base<SystemWebBrowser>() {

    override val LOG_TAG = "SystemWebBrowser"

    /**
     * Open the system web browser at the specified [url].
     * @param context Context.
     * @param url URL to show, it must be a complete URL, including HTTP or HTTPS, otherwise, it will not pass the validation.
     * @param case Reason that the URL was opened in the browser. This applies only if Firebase Analytics is enabled.
     *        When a URL is loaded in the browser the event is registered, this event contains a parameter that
     *        helps determine which website was shown.
     * */
    fun openUrl(context: Context, url: String, @Size(min = 1L, max = 100L) case: String = Event.ParameterValue.N_A) {

        // ******************************************************************************
        // TMP for ProGuard Test
        log("Android-Utils - Verbose FUNCTION **")
        logw("Android-Utils - Warning FUNCTION **")
        loge("Android-Utils - Error FUNCTION **")
        // TMP for ProGuard Test
        Log.v("SystemWebBrowser", "Android-Utils - Verbose direct >>")
        Log.w("SystemWebBrowser", "Android-Utils - Warning direct >>")
        Log.e("SystemWebBrowser", "Android-Utils - Error direct >>")
        // ******************************************************************************

        // URL validation
        require(URLUtil.isValidUrl(url)) {
            "The URL [$url] is not a valid URL"
        }

        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            val finalCase = if (case.trim().isBlank()) Event.ParameterValue.N_A else case.trim()
            firebaseAnalytics(Event.OPEN_URL_SYSTEM_WEB_BROWSER, Bundle().apply { putString(Event.Parameter.OPEN_URL_CASE, finalCase) })
            log("URL: [$url] opened, case: $finalCase")
        } catch (e: ActivityNotFoundException) {
            // No app can open the URL
            context.shortToast(R.string.system_web_browser_not_available)
            firebaseAnalytics(Event.OPEN_URL_SYSTEM_WEB_BROWSER, Bundle().apply { putString(Event.Parameter.OPEN_URL_CASE, "activity_not_found_exception") })
            logw("Unable to open URL [$url], web browser not available", e)
        } catch (e: Exception) {
            // General exception
            context.shortToast(R.string.system_web_browser_error)
            firebaseAnalytics(Event.OPEN_URL_SYSTEM_WEB_BROWSER, Bundle().apply { putString(Event.Parameter.OPEN_URL_CASE, "exception") })
            logw("Error opening URL [$url]", e)
        }
    }

}
