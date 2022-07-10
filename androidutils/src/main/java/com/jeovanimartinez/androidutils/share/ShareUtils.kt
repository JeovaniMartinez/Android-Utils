package com.jeovanimartinez.androidutils.share

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.annotation.Size
import androidx.core.content.FileProvider
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.analytics.Event
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes
import com.jeovanimartinez.androidutils.extensions.context.typeAsString
import java.io.File

// Reference: https://developer.android.com/training/sharing/send

/**
 * Utility to share text and files with other apps
 * */
object ShareUtils : Base<ShareUtils>() {

    override val LOG_TAG = "ShareUtils"

    /**
     * Provider to share files
     * */
    var fileProviderAuthority = ""
        set(value) {
            log("File Provider Authority = $value")
            field = value
        }

    /**
     * Share text with other apps.
     * @param activity Activity.
     * @param content Text to share, it can be a string or an id of a string resource.
     * @param chooserTitle Title for share chooser, it can be a string or an id of a string resource.
     * @param case Reason that the share action is called. This applies only if Firebase Analytics is enabled,
     *        the share event is registered and contains a parameter with the share case.
     * */
    fun shareText(
        activity: Activity,
        @StringOrStringRes content: Any,
        @StringOrStringRes chooserTitle: Any = R.string.share,
        @Size(min = 1L, max = 100L) case: String = Event.ParameterValue.N_A
    ) {
        doShare(activity, activity.typeAsString(chooserTitle), activity.typeAsString(content), case)
    }

    /**
     * Share a file (image, video, ect.) with other apps.
     * @param activity Activity.
     * @param file File to share.
     * @param chooserTitle Title for share chooser, it can be a string or an id of a string resource.
     * @param case Reason that the share action is called. This applies only if Firebase Analytics is enabled,
     *        the share event is registered and contains a parameter with the share case.
     * */
    fun shareFile(
        activity: Activity,
        file: File,
        @StringOrStringRes chooserTitle: Any = R.string.share,
        @Size(min = 1L, max = 100L) case: String = Event.ParameterValue.N_A
    ) {
        if (fileProviderAuthority.isBlank()) {
            throw IllegalStateException("You must specify the file provider (ShareUtils.fileProviderAuthority) before calling this function")
        }
        doShare(activity, activity.typeAsString(chooserTitle), file, case)
    }

    /**
     * Execute the action of sharing content
     * @param activity Activity.
     * @param content Content to share, must be type String or File.
     * @param chooserTitle Title for share chooser, it can be a string or an id of a string resource.
     * @param case Reason that the share action is called. This applies only if Firebase Analytics is enabled,
     *        the share event is registered and contains a parameter with the share case.
     * */
    private fun doShare(activity: Activity, chooserTitle: String, content: Any, case: String) {

        val finalCase = if (case.trim().isBlank()) Event.ParameterValue.N_A else case.trim()

        log(
            """
            Share invoked.
            Title: $chooserTitle
            Content: $content
            Case: $finalCase
        """.trimIndent()
        )

        val sendIntent = Intent(Intent.ACTION_SEND)

        // The type of content to share is determined and added to the intent
        when (content) {
            is String -> {
                sendIntent.type = "text/plain"
                sendIntent.putExtra(Intent.EXTRA_TEXT, content.toString())
            }
            is File -> {
                val extension = MimeTypeMap.getFileExtensionFromUrl(content.name)
                val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                log("File extension: $extension | Mime type: $mimeType")
                sendIntent.type = mimeType
                sendIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(activity, fileProviderAuthority, content))
            }
            else -> {
                throw IllegalArgumentException("content must be a String or File object")
            }
        }

        // The intent chooser is assigned, since in Android 5.1 and higher it is possible to determine which app the user chooses, Reference https://stackoverflow.com/a/50288268
        val intentChooser = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val intentReceiver = Intent(activity, ApplicationSelectorReceiver::class.java)
            intentReceiver.putExtra(ApplicationSelectorReceiver.EXTRA_SHARE_CASE_KEY, finalCase) // In order to determine the case in the broadcast
            val pendingIntent = PendingIntent.getBroadcast(activity, 0, intentReceiver, PendingIntent.FLAG_UPDATE_CURRENT)
            Intent.createChooser(sendIntent, chooserTitle, pendingIntent.intentSender)
        } else {
            Intent.createChooser(sendIntent, chooserTitle)
        }

        activity.startActivity(intentChooser) // Launch the chooser to share

        // Register the event
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            firebaseAnalytics(Event.SHARE_LAUNCHED, Bundle().apply { putString(Event.Parameter.SHARE_CASE, finalCase) })
        } else {
            firebaseAnalytics(Event.SHARE_OLD_API, Bundle().apply { putString(Event.Parameter.SHARE_CASE, finalCase) })
        }

    }

}
