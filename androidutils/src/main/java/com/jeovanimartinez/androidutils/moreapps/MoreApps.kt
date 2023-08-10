package com.jeovanimartinez.androidutils.moreapps

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.text.isDigitsOnly
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.analytics.Event
import com.jeovanimartinez.androidutils.extensions.context.shortToast

/**
 * Utility to invite the user to install more developer applications.
 * */
object MoreApps : Base<MoreApps>() {

    override val LOG_TAG = "MoreApps"

    /**
     * Directs the user to the developer's app list in Google Play, based on their developer ID.
     * If not possible, show a toast with a message.
     * @param activity Current activity from which the function is called.
     * @param developerId The developer ID, which is displayed when opening the developer page on
     *        Google Play, can be alphanumeric (developer name), for example, 'Jeo-Dev', or purely numeric,
     *        for example, '5700313618786177705'. For certain developer accounts, the numeric identifier may
     *        not work, so try it, and if it doesn't work, use the alphanumeric (developer name) one instead.
     * */
    fun showAppListInGooglePlay(activity: Activity, developerId: String) {

        try {

            /*
             * An attempt is made to open the developer's page in the system web browser (if the user has the Google Play app, it is shown there).
             * Avoid use intent.setPackage("com.android.vending") to open in the browser in case Google Play is not available.
             *
             * Reference: https://developer.android.com/distribute/marketing-tools/linking-to-google-play
             *
             * The URL is adjusted to work if the developer ID is provided as either a number or as text.
             */

            val webUriString = if (developerId.isDigitsOnly()) {
                "https://play.google.com/store/apps/dev?id=$developerId"
            } else {
                "https://play.google.com/store/apps/developer?id=$developerId"
            }

            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webUriString)))
            log("Sent user to view developer page in google play [$webUriString]")
            firebaseAnalytics(Event.MORE_APPS_SHOWN_GOOGLE_PLAY_OK)

        } catch (e: Exception) {

            // If it couldn't be shown developer's app list, a message is displayed on a toast
            activity.shortToast(R.string.more_apps_unable_to_show_dev_page)
            logw("Unable to send the user to developer page on google play", e)
            firebaseAnalytics(Event.MORE_APPS_SHOWN_GOOGLE_PLAY_ERROR)

        }

    }

}
