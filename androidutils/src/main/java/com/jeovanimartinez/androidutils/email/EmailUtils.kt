package com.jeovanimartinez.androidutils.email

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import androidx.annotation.Size
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.analytics.Event
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes
import com.jeovanimartinez.androidutils.extensions.context.longToast
import com.jeovanimartinez.androidutils.extensions.context.typeAsString
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull

/** Set of utilities for e-mails. */
object EmailUtils : Base<EmailUtils>() {

    override val LOG_TAG = "EmailUtils"

    /**
     * Launches an external app (using a chooser) to send an simple email through that app. If there is no app
     * available to send the email, show a toast, and if the recipient is not null, copy the recipient's email
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
        val finalRecipient = if (recipient == null) null else activity.typeAsString(recipient).trim()
        val finalSubject = activity.typeAsString(subject).trim()
        val finalContent = activity.typeAsString(content).trim()
        val finalChooserTitle = activity.typeAsString(chooserTitle).trim()

        @Suppress("ReplaceIsEmptyWithIfEmpty")
        val finalCase = if (case.trim().isBlank()) Event.ParameterValue.N_A else case.trim()

        log(
            """
            EmailUtils > sendEmailViaExternalApp() Data
            Recipient: $finalRecipient
            Subject: $finalSubject
            Content: $finalContent
            Chooser Title: $finalChooserTitle
            Case: $finalCase
            """.trimIndent()
        )

        // Email validation (if applicable)
        finalRecipient.whenNotNull {
            require(Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                "The recipient's email address [$it] is not a valid email"
            }
        }

        // If the action cannot be performed
        val handleNotAvailableAppOrException = fun() {

            // Show a toast
            val message = if (finalRecipient == null) activity.getString(R.string.email_utils_send_email_external_app_not_available_msg)
            else activity.getString(R.string.email_utils_send_email_external_app_not_available_msg_alt, finalRecipient)
            activity.longToast(message)

            // Copy the recipient's email address to the clipboard (if applicable)
            if (finalRecipient != null) {
                val clipboardManager = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.setPrimaryClip(ClipData.newPlainText(finalRecipient, finalRecipient))
            }
        }

        // Intent with the configuration
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            if (finalRecipient != null) putExtra(Intent.EXTRA_EMAIL, arrayOf(finalRecipient))
            putExtra(Intent.EXTRA_SUBJECT, finalSubject)
            putExtra(Intent.EXTRA_TEXT, finalContent)
        }

        // A list of all apps that can handle the intent action is obtained
        val intentActivitiesList: List<ResolveInfo> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.packageManager.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong()))
        } else {
            @Suppress("DEPRECATION")
            activity.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        }

        /*
        * This approach is used: intentActivitiesList.isNotEmpty() && intentActivitiesList[0].activityInfo.packageName != "com.android.fallback"
        * Instead of this: intent.resolveActivity(activity.packageManager) != null
        * This is because, sometimes, if there is no app that can handle the intent, it calls "com.android.fallback" and displays a message that
        * the action is unsupported. With this, the message is skipped and instead, a toast is shown if there is no installed app that can
        * handle the action.
        * Reference: https://www.mail-archive.com/android-developers@googlegroups.com/msg07564.html
        * */

        // It is checked whether the user has installed an app that can handle the action
        if (intentActivitiesList.isNotEmpty() && intentActivitiesList[0].activityInfo.packageName != "com.android.fallback") {
            try {
                activity.startActivity(Intent.createChooser(intent, finalChooserTitle))
                logAnalyticsEvent(Event.EMAIL_UTILS_SEND_EMAIL_EXTERNAL_APP, Bundle().apply {
                    putString(Event.Parameter.EMAIL_UTILS_SEND_EMAIL_EXTERNAL_APP_CASE, finalCase)
                })
                log("A chooser was launched to send an email with an external app")
            } catch (ex: Exception) {
                handleNotAvailableAppOrException()
                logAnalyticsEvent(Event.EMAIL_UTILS_SEND_EMAIL_EXTERNAL_APP, Bundle().apply {
                    putString(Event.Parameter.EMAIL_UTILS_SEND_EMAIL_EXTERNAL_APP_CASE, "exception")
                })
                loge("Unable to start action to send email", ex)
            }
        } else {
            handleNotAvailableAppOrException()
            logAnalyticsEvent(Event.EMAIL_UTILS_SEND_EMAIL_EXTERNAL_APP, Bundle().apply {
                putString(Event.Parameter.EMAIL_UTILS_SEND_EMAIL_EXTERNAL_APP_CASE, "no_app_available")
            })
            logw("There is no app available to send an email")
        }

    }

}
