package com.jeovanimartinez.androidutils.email

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import androidx.annotation.Size
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.analytics.Event
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes
import com.jeovanimartinez.androidutils.extensions.context.typeAsString
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull

/** Set of utilities for e-mails. */
object EmailUtils : Base<EmailUtils>() {

    override val LOG_TAG = "EmailUtils"

    /**
     * Launches an external app (using a chooser) to send an simple email through that app. If there is no application
     * available to send the email, show a toast and copy the recipient's email address to the clipboard.
     * @param activity Activity from which the process is initiated.
     * @param recipient Email address of the recipient.
     * @param subject Subject of the email.
     * @param content Content or body of the email.
     * @param chooserTitle Title to be displayed for the app chooser dialog.
     * @throws IllegalArgumentException If the provided recipient email is not a valid email.
     * */
    fun sendEmailViaExternalApp(
        activity: Activity,
        @StringOrStringRes recipient: Any? = null,
        @StringOrStringRes subject: Any = "",
        @StringOrStringRes content: Any = "",
        @StringOrStringRes chooserTitle: Any = "",
        @Size(min = 1L, max = 100L) case: String = Event.ParameterValue.N_A
    ) {

        // Email validation (if applicable)
        recipient.whenNotNull {
            require(Patterns.EMAIL_ADDRESS.matcher(activity.typeAsString(it)).matches()) {
                "The recipient's email address [$it] is not a valid email"
            }
        }

        // If the action cannot be performed
        val handleNotAvailableAppOrException = fun() {

        }

        // Intent with the configuration
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            if (recipient != null) putExtra(Intent.EXTRA_EMAIL, arrayOf(activity.typeAsString(recipient)))
            putExtra(Intent.EXTRA_SUBJECT, activity.typeAsString(subject))
            putExtra(Intent.EXTRA_TEXT, activity.typeAsString(content))
        }

        // It is checked whether the user has installed an app that can handle the action
        if (intent.resolveActivity(activity.packageManager) != null) {
            try {
                activity.startActivity(Intent.createChooser(intent, activity.typeAsString(chooserTitle)))
                @Suppress("ReplaceIsEmptyWithIfEmpty")
                val finalCase = if (case.trim().isBlank()) Event.ParameterValue.N_A else case.trim()
                firebaseAnalytics(Event.EMAIL_UTILS_SEND_EMAIL_EXTERNAL_APP, Bundle().apply {
                    putString(Event.Parameter.EMAIL_UTILS_SEND_EMAIL_EXTERNAL_APP_CASE, finalCase)
                })
                log("A chooser was launched to send an email with an external app")
            } catch (ex: Exception) {
                handleNotAvailableAppOrException()
                firebaseAnalytics(Event.EMAIL_UTILS_SEND_EMAIL_EXTERNAL_APP, Bundle().apply {
                    putString(Event.Parameter.EMAIL_UTILS_SEND_EMAIL_EXTERNAL_APP_CASE, "exception")
                })
                loge("Unable to start action to send email", ex)
            }
        } else {
            handleNotAvailableAppOrException()
            firebaseAnalytics(Event.EMAIL_UTILS_SEND_EMAIL_EXTERNAL_APP, Bundle().apply {
                putString(Event.Parameter.EMAIL_UTILS_SEND_EMAIL_EXTERNAL_APP_CASE, "no_app_available")
            })
            logw("There is no app available to send an email")
        }

    }

}
