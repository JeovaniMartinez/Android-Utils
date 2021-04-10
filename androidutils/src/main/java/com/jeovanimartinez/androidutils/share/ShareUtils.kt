package com.jeovanimartinez.androidutils.share

import android.app.Activity
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R
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
     * @param errorMessage Error message to display in a toast in case of the content cannot be shared,
     *        it can be a string or an id of a string resource.
     * @param case Reason that the share action is called. This applies only if Firebase Analytics is enabled,
     *        the share event is register and contains a parameter with share case.
     * */
    fun shareText(
        activity: Activity,
        @StringOrStringRes content: Any,
        @StringOrStringRes chooserTitle: Any = R.string.share,
        @StringOrStringRes errorMessage: Any = R.string.sharing_failed,
        case: String? = null
    ) {
        doShare(activity, activity.typeAsString(chooserTitle), activity.typeAsString(content), activity.typeAsString(errorMessage), case)
    }

    /**
     * Share a file (image, video, ect.) with other apps.
     * @param activity Activity.
     * @param file File to share.
     * @param chooserTitle Title for share chooser, it can be a string or an id of a string resource.
     * @param errorMessage Error message to display in a toast in case of the content cannot be shared,
     *        it can be a string or an id of a string resource.
     * @param case Reason that the share action is called. This applies only if Firebase Analytics is enabled,
     *        the share event is register and contains a parameter with share case.
     * */
    fun shareFile(
        activity: Activity,
        file: File,
        @StringOrStringRes chooserTitle: Any = R.string.share,
        @StringOrStringRes errorMessage: Any = R.string.sharing_failed,
        case: String? = null
    ) {
        if (fileProviderAuthority.isBlank()) {
            throw IllegalStateException("You must specify the file provider (ShareUtils.fileProviderAuthority) before calling this function")
        }
        doShare(activity, activity.typeAsString(chooserTitle), file, activity.typeAsString(errorMessage), case)
    }

    /**
     * Execute the action of sharing content
     * @param activity Activity.
     * @param content Content to share, must be type String or File.
     * @param chooserTitle Title for share chooser, it can be a string or an id of a string resource.
     * @param errorMessage Error message to display in a toast in case of the content cannot be shared,
     *        it can be a string or an id of a string resource.
     * @param case Reason that the share action is called. This applies only if Firebase Analytics is enabled,
     *        the share event is register and contains a parameter with share case.
     * */
    private fun doShare(activity: Activity, chooserTitle: String, content: Any, errorMessage: String, case: String?) {

        log(
            """
            Share invoked.
            Title: $chooserTitle
            Content: $content
            Case: $case
            Error message: $errorMessage
        """.trimIndent()
        )

        val sendIntent = Intent(Intent.ACTION_SEND)

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

        //val intentChooser = Intent.createChooser(sendIntent, chooserTitle)
        //activity.startActivity(intentChooser)

    }

}
