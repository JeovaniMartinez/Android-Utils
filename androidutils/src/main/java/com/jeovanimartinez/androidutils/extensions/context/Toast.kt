@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.context

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Extensions to show toast of short and long duration.
 */

/** Show a short toast with the indicated [message] (string resource ID). */
fun Context.shortToast(@StringRes message: Int) {
    showToast(message, Toast.LENGTH_SHORT)
}

/** Show a short toast with the indicated [message] (String). */
fun Context.shortToast(message: String) {
    showToast(message, Toast.LENGTH_SHORT)
}

/** Show a long toast with the indicated [message] (string resource ID). */
fun Context.longToast(@StringRes message: Int) {
    showToast(message, Toast.LENGTH_LONG)
}

/** Show a long toast with the indicated [message] (String). */
fun Context.longToast(message: String) {
    showToast(message, Toast.LENGTH_LONG)
}

private fun Context.showToast(message: Any, duration: Int) {
    Handler(Looper.getMainLooper()).post {
        val toast = when (message) {
            is Int -> Toast.makeText(this, message, duration)
            is String -> Toast.makeText(this, message, duration)
            else -> return@post
        }

        toast.show()
    }
}
