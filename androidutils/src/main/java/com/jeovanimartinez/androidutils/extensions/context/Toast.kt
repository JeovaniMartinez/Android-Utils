@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.context

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Extensions to show toast of short and long duration.
 * */

/** Show a short toast with the indicated [message] (the message must be the string resource ID) */
fun Context.shortToast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/** Show a short toast with the indicated [message] (the message must be a String) */
fun Context.shortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/** Show a long toast with the indicated [message] (the message must be the string resource ID) */
fun Context.longToast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/**  Show a long toast with the indicated [message] (the message must be a String) */
fun Context.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
