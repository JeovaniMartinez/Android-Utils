package com.jeovanimartinez.androidutils

import android.os.Bundle
import android.util.Log
import androidx.annotation.Size
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull

/**
 * Base class for the library utilities, with common properties and functions.
 * */
abstract class Base<T : Base<T>> {

    companion object {
        /**
         * To enable or disable the debug log messages, by default it is configured by BuildConfig.DEBUG, this configuration
         * applies to all classes that inherit from Base, that is, it is globally enabled or disabled.
         **/
        var logEnable = BuildConfig.DEBUG

        /**
         * Firebase Analytics instance, assign only if the app that implements the library should log analytics events, this property
         * is global and is used in all subclasses of this class. If you want to disable event logging for a specific class, do so
         * through the firebaseAnalyticsEnabled property of the class instance.
         * */
        var firebaseAnalyticsInstance: FirebaseAnalytics? = null

        // Note: These exceptions are not logged in the logging functions, because sending events to Firebase Crashlytics is only required in certain cases.
        /**
         * Firebase Crashlytics instance, assign only if the app implementing the library needs to send library crash reports to
         * Firebase Crashlytics, this property is global and is used in all subclasses of this class. Assign it only once within the
         * app, keeping in mind that the app must have Firebase Crashlytics configured, or leave it as null if it is not required
         * or if Firebase Crashlytics is not used in the app.
         * */
        var firebaseCrashlyticsInstance: FirebaseCrashlytics? = null
    }

    /**
     * Determines if the event log in Firebase Analytics is enabled for this class instance, for the event log, the static property of the companion
     * object [firebaseAnalyticsInstance] must also be assigned, if that property is null, no events will be logged, since what is required.
     * */
    @Suppress("MemberVisibilityCanBePrivate")
    var firebaseAnalyticsEnabled = true

    /** Log tag */
    @Suppress("PropertyName")
    protected abstract val LOG_TAG: String

    /**
     * Log an event in Firebase Analytics, as long as the base class has an instance of Firebase Analytics, and event logging is enabled for
     * the instance of the class.
     *
     * @param eventName Event name, must be between 1 and 40 characters.
     * @param eventParams Event parameters (optional).
     * */
    internal fun firebaseAnalytics(@Size(min = 1L, max = 40L) eventName: String, eventParams: Bundle? = null) {

        // No need to log the event in Firebase Analytics or show debug info, firebaseAnalyticsInstance is null
        if (firebaseAnalyticsInstance == null) return

        // If there is an assigned Firebase Analytics instance

        // Records the result of the event in the log, with the [message] indicating the action that was performed.
        val logResult = { message: String ->
            var params = "[N/A]" // To report in the log
            eventParams.whenNotNull { params = it.toString().replace("Bundle", "") }
            log("Event emitted: [ $eventName ] Params: $params | $message")
        }

        if (firebaseAnalyticsEnabled) {
            firebaseAnalyticsInstance!!.logEvent(eventName, eventParams)
            logResult("Event logged in Firebase Analytics")
        } else {
            logResult("No need to log the event in Firebase Analytics, this is disabled for this class instance")
        }

    }

    /**
     * Show the [message] and the [throwable] in the DEBUG log, used to detail the execution flow.
     **/
    internal fun log(message: Any, throwable: Throwable? = null) {
        if (!logEnable) return
        if (throwable != null) Log.d(LOG_TAG, message.toString(), throwable)
        else Log.d(LOG_TAG, message.toString())
    }

    /** Show the [message] and the [throwable] in the WARN log. */
    internal fun logw(message: Any, throwable: Throwable? = null) {
        if (throwable != null) Log.w(LOG_TAG, message.toString(), throwable)
        else Log.w(LOG_TAG, message.toString())
    }

    /** Show the [message] and the [throwable] in the ERROR log. */
    internal fun loge(message: Any, throwable: Throwable? = null) {
        if (throwable != null) Log.e(LOG_TAG, message.toString(), throwable)
        else Log.e(LOG_TAG, message.toString())
    }

}
