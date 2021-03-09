package com.jeovanimartinez.androidutils.moreapps

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.extensions.context.shortToast

/**
 * Utility to invite the user to install the developer apps.
 * */
object MoreApps : Base<MoreApps>() {

    override val LOG_TAG = "MoreApps"

    /** Developer ID whose list of apps is to be displayed when calling showAppListInGooglePlay. */
    var developerId = "Jedemm+Technologies"

    /**
     * Directs the user to the developer's app list in Google Play, based on their ID (developerId).
     * If not possible, show a toast with a message.
     *
     * @param activity Activity.
     * */
    fun showAppListInGooglePlay(activity: Activity) {
        try {
            // An attempt is made to open the developer's page in the system web browser (if the user has Google Play app, it is shown there)
            val webUriString = "https://play.google.com/store/apps/developer?id=$developerId"
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webUriString)))
            log("Sent user to view developer page in google play [$webUriString]")
            firebaseAnalytics("more_apps_sent_to_google_play", null)
        } catch (e: ActivityNotFoundException) {
            // If it couldn't be shown developer's app list, a message is displayed into a toast
            activity.shortToast(R.string.more_apps_unable_to_show_dev_page)
            logw("Unable to send user to developer page", e)
            firebaseAnalytics("more_apps_unable_to_show_dev_page", null)
        }
    }

}
