package com.jeovanimartinez.androidutils.logutils

import android.util.Log
import androidx.annotation.Size
import com.jeovanimartinez.androidutils.BuildConfig

/**
 * Utility for sending log output. It's similar and uses [android.util.Log] to send log output, but it offers greater flexibility.
 * */
object Log {

    /**
     * To enable or disable the log messages, by default it is configured by BuildConfig.DEBUG
     **/
    var logEnable = BuildConfig.DEBUG

    /**
     * Send a VERBOSE log message.
     * @param message The message to be logged. It can be of any data type and the value is always converted to a string for display.
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     *        Set or leave as null to use the name of the class that calls this function as the tag.
     * */
    fun logv(message: Any, @Size(max = 23L) tag: String? = null) {
        if (!logEnable) return
        val finalTag = tag ?: generateLogTag()
        Log.v(finalTag, message.toString())
    }

    /**
     * Send a VERBOSE log message.
     * @param message The message to be logged. It can be of any data type and the value is always converted to a string for display.
     * @param throwable The exception to be logged.
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     *        Set or leave as null to use the name of the class that calls this function as the tag.
     * */
    fun logv(message: Any?, throwable: Throwable, @Size(max = 23L) tag: String? = null) {
        if (!logEnable) return
        val finalTag = tag ?: generateLogTag()
        Log.v(finalTag, message.toString(), throwable)
    }

    /**
     * Generates the log tag using the name of the class that invoked the logging function as the tag.
     * @return The log tag.
     * */
    private fun generateLogTag(): String {
        val stackTrace = Thread.currentThread().stackTrace
        val fullClassName = stackTrace[4].className // Full class name, including the package name
        var className = fullClassName.substringAfterLast('.')

        /*
        * Special cases where adjusting the tag is required.
        * */

        // If it's this same class, the stackTrace position is incremented to obtain the correct name
        if (fullClassName == "com.jeovanimartinez.androidutils.logutils.Log") {
            className = stackTrace[5].className.substringAfterLast('.')
        }

        return className
    }

}
