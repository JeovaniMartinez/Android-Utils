package com.jeovanimartinez.androidutils.email

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import androidx.annotation.Size
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.analytics.Event
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull

/** Set of utilities for e-mails. */
object EmailUtils : Base<EmailUtils>() {

    override val LOG_TAG = "EmailUtils"

    /**
     * Launches an external app (using a chooser) to send an simple email through that app. If there is no application
     * available to send the email, show a toast and copy the recipient's email address to the clipboard.
     * @param activity Activity from which the process is initiated.
     * @param recipient Email address of the recipient (optional).
     * @param subject Subject of the email (optional).
     * @param content Content or body of the email (optional).
     * @param chooserTitle Title to be displayed for the app chooser dialog (optional).
     * */
    fun sendEmailViaExternalApp(
        activity: Activity,
        recipient: String? = null,
        subject: String = "",
        content: String = "",
        chooserTitle: String = "",
        @Size(min = 1L, max = 100L) case: String = Event.ParameterValue.N_A
    ) {

        // Email validation (if applicable)
        recipient.whenNotNull {
            require(Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                "The recipient's email address [$it] is not a valid email"
            }
        }

        // If the action cannot be performed
        val handleNotAvailableAppOrException = fun() {

        }

        // Intent with the configuration
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, content)
        }

        // It is checked whether the user has installed an app that can handle the action
        if (intent.resolveActivity(activity.packageManager) != null) {
            try {
                activity.startActivity(Intent.createChooser(intent, chooserTitle))
            } catch (ex: Exception) {
                handleNotAvailableAppOrException()
            }
        } else {

        }

    }

}
