package com.jeovanimartinez.androidutils.about

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.about.config.AboutAppConfig
import com.jeovanimartinez.androidutils.analytics.Event

/**
 * Utility to show an about app activity.
 * */
object AboutApp : Base<AboutApp>() {

    override val LOG_TAG = "AboutApp"

    /**
     * It is assigned before starting AboutActivity to be able to use it in that activity, it is used in this way since
     * AboutAppConfig contains non-serializable objects, and putting the object in intent extras is not possible.
     * */
    internal var currentConfig: AboutAppConfig? = null

    /**
     * Shows an about activity.
     * @param activity Current activity from which the about activity will be launched.
     * @param aboutAppConfig Configuration object for the about activity.
     * */
    fun show(activity: Activity, aboutAppConfig: AboutAppConfig) {

        // If the activity is already launched
        if (AboutActivity.aboutActivityRunning) {
            log("AboutActivity is running and only one instance of this activity is allowed")
            return
        }

        currentConfig = aboutAppConfig // Set the current configuration

        AboutActivity.aboutActivityRunning = true // Set to true since the activity is going to start

        // Launch about activity, currentConfig is already assigned to be used
        activity.startActivity(Intent(activity, AboutActivity::class.java), ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())

        firebaseAnalytics(Event.ABOUT_APP_SHOWN) // The event is registered here, to avoid registering more than once in the activity (in case it is recreated)
        log("Launched AboutActivity")
    }

}
