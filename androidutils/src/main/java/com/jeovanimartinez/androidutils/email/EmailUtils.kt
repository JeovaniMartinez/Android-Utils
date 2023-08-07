package com.jeovanimartinez.androidutils.email

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import androidx.annotation.Size
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.analytics.Event
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes
import com.jeovanimartinez.androidutils.extensions.context.typeAsString
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull

/** Set of utilities for e-mails. */
object EmailUtils : Base<EmailUtils>() {

    override val LOG_TAG = "EmailUtils"

    /**
     * Launches an external app (using a chooser) to send an simple email through that app. If there is no app
     * available to send the email, show a toast and if the recipient is not null, copy the recipient's email
     * address to the clipboard.
     * @param activity Activity from which the process is initiated.
     * @param recipient Email address of the recipient. It is possible to specify the field or leave it as null
     *        for the user to enter it manually in the external app.
     * @param subject Subject of the email.
     * @param content Content or body of the email.
     * @param chooserTitle Title for the app chooser dialog.
     * @param case Reason for performing the action. Only applies if Firebase Analytics is enabled. This event will
     *        only be logged if the chooser is launched successfully, but it won't log if the chooser is canceled
     *        or the specific action performed by the user. It also logs if an error occurs and the chooser cannot
     *        be launched.
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

        // The data is processed and the final values are generated
        val finalRecipient = if (recipient == null) null else activity.typeAsString(recipient)
        val finalSubject = activity.typeAsString(subject)
        val finalContent = activity.typeAsString(content)
        val finalChooserTitle = activity.typeAsString(chooserTitle)

        @Suppress("ReplaceIsEmptyWithIfEmpty")
        val finalCase = if (case.trim().isBlank()) Event.ParameterValue.N_A else case.trim()

        // Email validation (if applicable)
        finalRecipient.whenNotNull {
            require(Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                "The recipient's email address [$it] is not a valid email"
            }
        }

        // If the action cannot be performed
        val handleNotAvailableAppOrException = fun() {
            //val message = if (recipient != null) activity.getString(R.string.email_utils_send_email_external_app_not_available_msg)
            //else activity.getString(R.string.email_utils_send_email_external_app_not_available_msg)
        }

        // Intent with the configuration
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            if (recipient != null) putExtra(Intent.EXTRA_EMAIL, arrayOf(finalRecipient))
            putExtra(Intent.EXTRA_SUBJECT, finalSubject)
            putExtra(Intent.EXTRA_TEXT, finalContent)
        }

        // It is checked whether the user has installed an app that can handle the action
        if (intent.resolveActivity(activity.packageManager) != null) {
            try {
                activity.startActivity(Intent.createChooser(intent, finalChooserTitle))
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
