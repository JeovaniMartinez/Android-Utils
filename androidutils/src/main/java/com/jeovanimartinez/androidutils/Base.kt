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

        // Note: This exceptions are not logged into log functions, because sending events to Firebase Crashlytics is only required in certain cases.
        /**
         * Firebase Crashlytics instance, assign only if the app implementing the library need to log library recoverable errors in
         * Firebase Crashlytics, this property is global and is used in all subclasses of this class. Assign it only once within the
         * app, taking into account that the app must have Firebase Crashlytics configured, or leave it as null if it is not required
         * or if Firebase Crashlytics is not used in the app.
         * */
        var firebaseCrashlyticsInstance: FirebaseCrashlytics? = null
    }

    /**
     * Determines if the event log in Firebase Analytics is enabled for the class instance, for the event log, the static property of the companion
     * object firebaseAnalyticsInstance must also be assigned, if that property is null, no events will be logged, since what is required.
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

        // Records the result of the event in the log, with the [message] indicating the action that was performed.
        val logResult = { message: String ->
            var params = "[N/A]" // To report in the log
            eventParams.whenNotNull { params = it.toString().replace("Bundle", "") }
            log("Event emitted: [ $eventName ] Params: $params | $message")
        }

        if (firebaseAnalyticsInstance == null) return logResult("No need to log event into Firebase Analytics, firebaseAnalyticsInstance is null")

        if (!firebaseAnalyticsEnabled) return logResult("No need to log event into Firebase Analytics, this is disabled for this class instance")

        // Otherwise, the event is logged in Firebase Analytics.
        firebaseAnalyticsInstance!!.logEvent(eventName, eventParams)
        logResult("Event logged into Firebase Analytics")

    }

    /**
     * Show the [message] and the [throwable] into DEBUG log, used to detail the execution flow.
     **/
    internal fun log(message: Any, throwable: Throwable? = null) {
        if (!logEnable) return
        if (throwable != null) Log.d(LOG_TAG, message.toString(), throwable)
        else Log.d(LOG_TAG, message.toString())
    }

    /** Show the [message] and the [throwable] into WARN log. */
    internal fun logw(message: Any, throwable: Throwable? = null) {
        if (throwable != null) Log.w(LOG_TAG, message.toString(), throwable)
        else Log.w(LOG_TAG, message.toString())
    }

    /** Show the [message] and the [throwable] into ERROR log. */
    internal fun loge(message: Any, throwable: Throwable? = null) {
        if (throwable != null) Log.e(LOG_TAG, message.toString(), throwable)
        else Log.e(LOG_TAG, message.toString())
    }

}
