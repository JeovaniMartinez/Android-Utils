package com.jeovanimartinez.androidutils.web

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
     * Open the default system web browser at the specified [url].
     * @param context Context.
     * @param url URL to be opened, it must be a complete URL, including HTTP or HTTPS, otherwise, it will not pass the validation.
     * @param case Reason that the URL was opened in the browser. This applies only if Firebase Analytics is enabled.
     *        When a URL is loaded in the browser the event is registered, this event contains a parameter that
     *        helps determine which website was opened.
     * @throws IllegalArgumentException If the provided URL is not a valid URL.
     * */
    fun openUrl(context: Context, url: String, @Size(min = 1L, max = 100L) case: String = Event.ParameterValue.N_A) {

        // No chooser is showed to select a web browser; instead, it opens directly in the default web browser.

        // URL validation
        require(URLUtil.isValidUrl(url)) {
            "The URL [$url] is not a valid URL"
        }

        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            @Suppress("ReplaceIsEmptyWithIfEmpty")
            val finalCase = if (case.trim().isBlank()) Event.ParameterValue.N_A else case.trim()
            logAnalyticsEvent(Event.SYSTEM_WEB_BROWSER_OPEN_URL, Bundle().apply { putString(Event.Parameter.SYSTEM_WEB_BROWSER_OPEN_URL_CASE, finalCase) })
            log("URL: [$url] opened, case: $finalCase")
        } catch (e: ActivityNotFoundException) {
            // No app can open the URL
            context.shortToast(R.string.system_web_browser_not_available)
            logAnalyticsEvent(Event.SYSTEM_WEB_BROWSER_OPEN_URL, Bundle().apply { putString(Event.Parameter.SYSTEM_WEB_BROWSER_OPEN_URL_CASE, "activity_not_found_exception") })
            logw("Unable to open URL [$url], web browser not available", e)
        } catch (e: Exception) {
            // General exception
            context.shortToast(R.string.system_web_browser_error)
            logAnalyticsEvent(Event.SYSTEM_WEB_BROWSER_OPEN_URL, Bundle().apply { putString(Event.Parameter.SYSTEM_WEB_BROWSER_OPEN_URL_CASE, "exception") })
            loge("Error opening URL [$url]", e)
        }
    }

}
