package com.jeovanimartinez.androidutils.about

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.extensions.nullability.isNull

/**
 * Utility to show an app about activity.
 * */
object AboutApp : Base<AboutApp>() {

    override val LOG_TAG = "AboutApp"

    /**
     * It is assigned before starting AboutActivity to be able to use it in that activity, it is used in this way since
     * AboutAppConfig contains non-serializable objects, and putting the object in intent extras is not possible.
     * */
    internal var currentConfig: AboutAppConfig? = null

    /**
     * Shows about activity.
     * @param activity Activity.
     * @param aboutAppConfig Configuration object for the about activity.
     * */
    fun show(activity: Activity, aboutAppConfig: AboutAppConfig) {

        // If the activity is already launched
        if (AboutActivity.aboutActivityRunning) {
            log("AboutActivity is running and only one instance of this activity is allowed")
            return
        }

        // Obtains the color from the config
        var backgroundColor = aboutAppConfig.backgroundColor
        var iconsColor = aboutAppConfig.iconsColor
        var termsAndPrivacyPolicyTextColor = aboutAppConfig.termsAndPrivacyPolicyTextColor
        // aboutAppConfig.taskDescriptionColor It is not necessary, because it value not is mandatory, and it cant be null

        // For the null colors, get the default color.
        if (backgroundColor.isNull()) backgroundColor = activity.getColorCompat(R.color.colorBackground)
        if (iconsColor.isNull()) iconsColor = activity.getColorCompat(R.color.colorIcon)
        if (termsAndPrivacyPolicyTextColor.isNull()) termsAndPrivacyPolicyTextColor = activity.getColorCompat(R.color.colorTermsAndPrivacyPolicyText)

        // The final configuration object is generated and assigned to the singleton to be able to use the data in the AboutActivity
        currentConfig = aboutAppConfig.copy(backgroundColor = backgroundColor, iconsColor = iconsColor, termsAndPrivacyPolicyTextColor = termsAndPrivacyPolicyTextColor)

        AboutActivity.aboutActivityRunning = true // It is true since the activity is going to start

        // Launch about activity, currentConfig is already assigned to be use
        activity.startActivity(
            Intent(activity, AboutActivity::class.java),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ActivityOptions.makeSceneTransitionAnimation(activity).toBundle() else null
        )

        firebaseAnalytics("about_app_shown") // The event is registered here, to avoid registering more than once in the activity (in case it is recreated)
        log("Launched AboutActivity")
    }

}
