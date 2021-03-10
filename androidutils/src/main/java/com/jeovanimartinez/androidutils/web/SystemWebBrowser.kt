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
     * @param url URL to show, it must be a complete URL, including http or https, otherwise it will not pass the validation
     * @param case Reason that the URL was opens in the browser. This applies only if Firebase Analytics is enabled.
     *        When a URL is loaded in the browser the event is registered, this event contains a parameter that
     *        helps determine which website was shown.
     * */
    fun openUrl(context: Context, url: String, @Size(min = 1L, max = 100L) case: String? = null) {

        if (!URLUtil.isValidUrl(url)) return loge("The URL [$url] is not a valid URL")

        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            // The event is logged
            if (case != null && case.isNotBlank()) {
                firebaseAnalytics("open_url_in_system_web_browser", Bundle().apply { putString("open_url_case", case.trim()) })
            } else {
                firebaseAnalytics("open_url_in_system_web_browser")
            }
            log("URL: [$url] opened, case: ${case?.trim()}")
        } catch (e: ActivityNotFoundException) {
            // There is no app that can open the URL
            context.shortToast(R.string.system_web_browser_not_available)
            firebaseAnalytics("open_url_in_system_web_browser", Bundle().apply { putString("open_url_case", "activity_not_found_exception") })
            logw("Unable to open URL [$url], web browser not available", e)
        } catch (e: Exception) {
            // General exception
            context.shortToast(R.string.system_web_browser_error)
            firebaseAnalytics("open_url_in_system_web_browser", Bundle().apply { putString("open_url_case", "exception") })
            loge("Error opening URL [$url]", e)
        }
    }

}
