package com.jeovanimartinez.androidutils.reviews

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import com.google.android.play.core.review.ReviewManagerFactory
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.analytics.Event
import com.jeovanimartinez.androidutils.extensions.context.shortToast
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull
import com.jeovanimartinez.androidutils.logutils.Log.loge
import com.jeovanimartinez.androidutils.logutils.Log.logv
import com.jeovanimartinez.androidutils.logutils.Log.logw
import java.lang.IllegalStateException
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Set of utilities to invite the user to rate the app.
 * */
object RateApp : Base<RateApp>() {

    override val LOG_TAG = "RateApp"

    // Time elapsed from when the flow was obtained until it was completed to consider that the flow was shown
    private const val REVIEW_FLOW_SHOWN_MIN_ELAPSED_TIME = 2000 // In milliseconds

    /** Object with constants for the preferences. */
    internal object Preferences {
        const val KEY = "androidutils_rate_app_preferences" // Filename
        const val CONFIGURED = "rate_app_configured"
        const val LAUNCH_COUNTER = "rate_app_launch_counter"
        const val LAST_SHOW_DATE = "rate_app_last_show_date" // Time in milliseconds since January 1, 1970, 00:00:00 GMT
        const val FLOW_SHOWN_COUNTER = "rate_app_flow_shown_counter"
    }

    /**
     * The minimum number of days required since the app was installed to be able to show the flow, it is used in combination with minInstallLaunchTimes,
     * and both conditions must be met to show the flow, the minimum value is 0 (it is shown from that same day).
     * */
    var minInstallElapsedDays = 10
        set(value) {
            if (initialized && value != field) {
                /*
                * If it's already initialized and the value intended to be assigned is different from the current one, an exception is
                * thrown since changing the value is not allowed. If the value to be assigned is the same as the current one, nothing
                * happens, and the assignment is permitted as it will remain unchanged.
                * */
                throw IllegalStateException("RateApp is already initialized. It is no longer possible to change the value of minInstallElapsedDays")
            }
            field = validateConfigValue(value, 0)
        }

    /**
     * The minimum number of times the app must have been launched since it was installed to be able to show the flow, it is used in combination with
     * minInstallElapsedDays, and both conditions must be met to show the flow, the minimum value is 1 (shown from the first launch).
     * */
    var minInstallLaunchTimes = 10
        set(value) {
            if (initialized && value != field) {
                /*
                * If it's already initialized and the value intended to be assigned is different from the current one, an exception is
                * thrown since changing the value is not allowed. If the value to be assigned is the same as the current one, nothing
                * happens, and the assignment is permitted as it will remain unchanged.
                * */
                throw IllegalStateException("RateApp is already initialized. It is no longer possible to change the value of minInstallLaunchTimes")
            }
            field = validateConfigValue(value, 1)
        }

    /**
     * The minimum number of days required since the flow was shown to show it again, it is used in combination with minRemindLaunchTimes, and both
     * conditions must be met to show the flow, the minimum value is 0 (it is shown from that same day).
     * */
    var minRemindElapsedDays = 2
        set(value) {
            if (initialized && value != field) {
                /*
                * If it's already initialized and the value intended to be assigned is different from the current one, an exception is
                * thrown since changing the value is not allowed. If the value to be assigned is the same as the current one, nothing
                * happens, and the assignment is permitted as it will remain unchanged.
                * */
                throw IllegalStateException("RateApp is already initialized. It is no longer possible to change the value of minRemindElapsedDays")
            }
            field = validateConfigValue(value, 0)
        }

    /**
     * The minimum number of times the app must have been launched since it showed the flow to show it again, it is used in combination with
     * minRemindElapsedDays, and both conditions must be met to show the flow, the minimum value is 1 (it is shown from the first launch).
     * */
    var minRemindLaunchTimes = 4
        set(value) {
            if (initialized && value != field) {
                /*
                * If it's already initialized and the value intended to be assigned is different from the current one, an exception is
                * thrown since changing the value is not allowed. If the value to be assigned is the same as the current one, nothing
                * happens, and the assignment is permitted as it will remain unchanged.
                * */
                throw IllegalStateException("RateApp is already initialized. It is no longer possible to change the value of minRemindLaunchTimes")
            }
            field = validateConfigValue(value, 1)
        }

    /**
     * Indicates in which call to checkAndShow() the flow will be shown.
     *
     * For example, if you want to show the flow in the onResume() of the main activity:
     * - RateApp.showAtEvent = 1: In this case, the flow will be shown in the first call to onResume(), which is when the activity is launched.
     * - RateApp.showAtEvent = 2: In this case, the flow will be shown in the second call to the activity's onResume().
     * */
    var showAtEvent = 2
        set(value) {
            if (initialized && value != field) {
                /*
                * If it's already initialized and the value intended to be assigned is different from the current one, an exception is
                * thrown since changing the value is not allowed. If the value to be assigned is the same as the current one, nothing
                * happens, and the assignment is permitted as it will remain unchanged.
                * */
                throw IllegalStateException("RateApp is already initialized. It is no longer possible to change the value of showAtEvent")
            }
            field = validateConfigValue(value, 1)
        }

    /** Ensures that the config [value] is valid and returns the same value for the easier assignment. */
    private fun validateConfigValue(value: Int, minValue: Int): Int {
        if (value < minValue) throw IllegalArgumentException("Invalid config value, value ($value) must be equal to or greater than $minValue")
        return value
    }

    private var initialized = false // Helper to determine if init was already called
    private var checkShowEventCount = 0 // Helper to count how many times checkAndShow() has been called
    private var validated = false // Determines if it has already been validated if the flow should be shown, to only validate it once per session

    /**
     * Initialize and configure the utility, it must always be called only once from the app.
     * @param context Context for initializing the utility.
     * */
    fun init(context: Context) {
        if (initialized) {
            logw(
                """
                    This call to RateApp.init() HAS BEEN IGNORED AND WILL HAVE NO EFFECT, 
                    as RateApp.init() should only be called once when the application starts.
                """.trimIndent()
            )
            return
        }

        // Configure the preferences and values
        val sharedPreferences = context.applicationContext.getSharedPreferences(Preferences.KEY, Context.MODE_PRIVATE)
        configurePreferences(sharedPreferences)
        updateLaunchCounter(sharedPreferences)

        initialized = true

        logv(
            """
            RateApp initialized
            minInstallElapsedDays: $minInstallElapsedDays
            minInstallLaunchTimes: $minInstallLaunchTimes
            minRemindElapsedDays: $minRemindElapsedDays
            minRemindLaunchTimes: $minRemindLaunchTimes
            showAtEvent: $showAtEvent
            """.trimIndent()
        )
    }

    /**
     * Checks if all conditions to show the flow are met, and shows the flow only if all conditions are met.
     * @param activity Activity from which the process starts.
     * */
    fun checkAndShow(activity: Activity) {
        if (!initialized) {
            // It's necessary to call init before calling this function
            throw IllegalStateException("Need to call RateApp.init() before calling this function")
        }

        if (validated) {
            logv("The conditions have already been validated in this session")
            return
        }

        checkShowEventCount++

        // If it does not apply to show in this call to the event
        if (showAtEvent != checkShowEventCount) {
            logv("No need verify conditions in this call, showAtEvent: $showAtEvent | checkShowEventCount  $checkShowEventCount ")
            return
        }

        logv("We proceed to verify the conditions to show the flow to rate the app, check and show event: $showAtEvent")
        doCheckAndShow(activity) // Execute the full verification

        validated = true // In the end, it is indicated that it is already valid, to only do it once per session
    }

    /**
     * Set the preferences to the default values in case they are not already configured.
     * @param sharedPreferences Shared Preferences for handling the preferences.
     * */
    private fun configurePreferences(sharedPreferences: SharedPreferences) {
        if (!sharedPreferences.getBoolean(Preferences.CONFIGURED, false)) {
            with(sharedPreferences.edit()) {
                putInt(Preferences.LAUNCH_COUNTER, 0)
                putLong(Preferences.LAST_SHOW_DATE, Date().time) // In the first time, it is set with the current date, to have a reference value
                putInt(Preferences.FLOW_SHOWN_COUNTER, 0)
                putBoolean(Preferences.CONFIGURED, true)
                apply()
            }
            logv("The preferences are configured for the first time")
        } else {
            logv("The preferences are already configured")
        }
    }

    /**
     * Update the app launch counter.
     * @param sharedPreferences Shared Preferences for handling the preferences.
     * */
    private fun updateLaunchCounter(sharedPreferences: SharedPreferences) {
        val currentValue = sharedPreferences.getInt(Preferences.LAUNCH_COUNTER, 0)
        val newValue = currentValue + 1
        sharedPreferences.edit().putInt(Preferences.LAUNCH_COUNTER, newValue).apply()
        logv("Updated launch counter value from $currentValue to $newValue")
    }

    /**
     * Execute the verification and show the flow to rate the app, call after executing the first checks in checkAndShow(),
     * the functions were separated for better code structure.
     * @param activity Activity from which the process starts.
     * */
    private fun doCheckAndShow(activity: Activity) {

        logv("Invoked > doCheckAndShow()")

        val sharedPreferences = activity.applicationContext.getSharedPreferences(Preferences.KEY, Context.MODE_PRIVATE)

        // First, preferences for reference values are loaded
        val launchCounter = sharedPreferences.getInt(Preferences.LAUNCH_COUNTER, 1)
        val lastShowDateValue = sharedPreferences.getLong(Preferences.LAST_SHOW_DATE, 0)
        val lastShowDate = Date(lastShowDateValue)
        val flowShowCounter = sharedPreferences.getInt(Preferences.FLOW_SHOWN_COUNTER, 0)

        val minElapsedDays: Int
        val minLaunchTimes: Int

        if (flowShowCounter == 0) {
            // If the flow has never shown, the values are assigned according to the installation values
            minElapsedDays = minInstallElapsedDays
            minLaunchTimes = minInstallLaunchTimes
            logv("Values configured by install values")
        } else {
            // If the flow has already been shown at least once, the values are assigned based on the reminder values
            minElapsedDays = minRemindElapsedDays
            minLaunchTimes = minRemindLaunchTimes
            logv("Values configured by remind values")
        }

        // Show values for development purposes, the date is displayed in a local format for better understanding
        logv(
            """
            Current Values
            launchCounter: $launchCounter
            lastShowDateValue $lastShowDateValue
            lastShowDate: ${DateFormat.getDateTimeInstance().format(lastShowDate)}
            flowShowCounter: $flowShowCounter
            minElapsedDays: $minElapsedDays
            minLaunchTimes: $minLaunchTimes
            """.trimIndent()
        )

        // Check app launches
        val launchCounterAreMet = if (launchCounter < minLaunchTimes) {
            logv("The condition of minimum required launches is not met, a minimum of $minLaunchTimes launches is required, current: $launchCounter")
            false
        } else {
            logv("The condition of minimum required launches is met, current: $launchCounter | required: $minLaunchTimes")
            true
        }

        // Calculate elapsed days between the last date the flow was shown and the current date
        val elapsedDays = ((Date().time - lastShowDateValue) / TimeUnit.DAYS.toMillis(1)).toInt()
        logv("Elapsed days between the last date of the review flow showed and today is: $elapsedDays")

        var elapsedDaysAreMet = false // Initial value

        // If the elapsed days are negative, it indicates an alteration in the date of the device, so the value of LAST_SHOW_DATE is reset
        if (elapsedDays < 0) {
            sharedPreferences.edit().putLong(Preferences.LAST_SHOW_DATE, Date().time).apply()
            logv("Elapsed days ($elapsedDays) value is negative and invalid, the value is restarted to the current date")
        } else {
            // Check elapsed days
            elapsedDaysAreMet = if (elapsedDays < minElapsedDays) {
                logv("The condition of minimum elapsed days is not met, a minimum of $minElapsedDays days elapsed is required, current: $elapsedDays")
                false
            } else {
                logv("The condition of minimum elapsed days is met, current: $elapsedDays | required: $minElapsedDays")
                true
            }
        }

        // If any of the conditions are not met
        if (!launchCounterAreMet || !elapsedDaysAreMet) {
            logv(
                "Not all conditions are met [launchCounterAreMet = $launchCounterAreMet] [elapsedDaysAreMet = $elapsedDaysAreMet], " +
                        "It is not necessary to show flow to rate the app"
            )
            return
        }

        // If all conditions are met, the flow to rate app must be shown

        logv("All conditions are met, the flow to rate the app must be shown")

        rateWithInAppReviewApi(activity)

    }

    /*
    *
    * Reference: https://developer.android.com/guide/playcore/in-app-review
    *
    * To test failure event, disable the Google Play Store app on the test device.
    *
    * To force the review flow to be shown if it was just shown (for testing):
    *   - Turn off the internet.
    *   - Clear data and cache of the Google Play Store app.
    *   - Open the test app and invoke the flow (it won't be shown).
    *   - Close the test app.
    *   - Turn on the internet.
    *   - Open the test app again and invoke the flow (it should be shown).
    *
    * */
    /**
     * Show the flow to rate the app with Google Play In-App Review API.
     * @param activity Activity from which the process starts.
     * */
    private fun rateWithInAppReviewApi(activity: Activity) {

        logv("Invoked > rateWithInAppReviewApi()")
        logAnalyticsEvent(Event.RATE_APP_REQUEST_REVIEW_FLOW)

        val reviewManager = ReviewManagerFactory.create(activity)
        val managerRequest = reviewManager.requestReviewFlow()

        // First, it's necessary to create the request
        managerRequest.addOnCompleteListener { request ->

            if (request.isSuccessful) {

                // If the request was created successfully
                var successfulReviewFlow = false // Determine if the flow was correct
                val flowObtainedTime = Date().time // Assign the date was the flow is obtained
                logv("requestReviewFlow() > Success")
                val reviewInfo = request.result // Get the result
                val reviewFlow = reviewManager.launchReviewFlow(activity, reviewInfo) // The flow to rate the app is launched

                // The review flow process was successful
                reviewFlow.addOnSuccessListener {
                    /*
                     * The flow was correct, considering that the API does not report if the user rated the app or not,
                     * nor does it report if the flow was shown or not, so it only indicates that the process was successful.
                     *
                     * Considerations:
                     *   - It is not possible to know if the flow was shown or not, nor the result (if the app was rated or not)
                     *   - If the user has already rated the app, the flow is not displayed, but is called here
                     *   - If the user still does not rate the app, but the quota to show the flow has already been exceeded, the flow is not shown
                     */
                    successfulReviewFlow = true // The flow was correct
                    logv("launchReviewFlow() > Success")
                    updatePreferencesOnFlowShown(activity.applicationContext) // Preferences are updated because the flow is complete
                    logAnalyticsEvent(Event.RATE_APP_REVIEW_FLOW_OK)
                }

                // Review flow error
                reviewFlow.addOnFailureListener {
                    validated = false // It is returned to false, to try again in this session, since the flow could not be shown
                    checkShowEventCount = showAtEvent - 1 // Adjust to try again on the next event
                    loge("launchReviewFlow() > Error", it)
                }

                // Flow completed
                reviewFlow.addOnCompleteListener {
                    logv("Finished process to rate the app with Google Play In-App Review API")
                    /*
                    * If the flow result was correct.
                    *
                    * The API does not report if the flow was displayed or not, but according to tests performed, this can be inferred by determining the
                    * time from when the flow was obtained until it was completed, if it takes a certain time, it is most likely that the flow has been
                    * shown, it is not a 100% accurate, but it is inferred by the elapsed time.
                    *
                    * Registering whether the flow was shown or not is very useful for analytics events, since it allows you to analyze and adjust the settings
                    * of when it is convenient to show the flow to obtain the highest number of reviews in the app without disturbing the user.
                    * */
                    if (successfulReviewFlow) {
                        val elapsedTime = Date().time - flowObtainedTime
                        logv("Elapsed time in review flow ${elapsedTime / 1000.0} seconds ($elapsedTime milliseconds)")
                        if (elapsedTime >= REVIEW_FLOW_SHOWN_MIN_ELAPSED_TIME) {
                            logv(
                                "Elapsed time ${elapsedTime / 1000.0} is greater or equal to ${REVIEW_FLOW_SHOWN_MIN_ELAPSED_TIME / 1000.0}, " +
                                        "it is considered that the flow was shown to the user"
                            )
                            logAnalyticsEvent(Event.RATE_APP_REVIEW_FLOW_SHOWN)
                        } else {
                            logv(
                                "Elapsed time ${elapsedTime / 1000.0} is less than ${REVIEW_FLOW_SHOWN_MIN_ELAPSED_TIME / 1000.0}, " +
                                        "it is considered that the flow NO was shown to the user"
                            )
                        }
                    }
                }

            } else {
                validated = false // It is returned to false, to try again in this session, since the flow could not be shown
                checkShowEventCount = showAtEvent - 1 // Adjust to try again on the next event
                request.exception.whenNotNull { loge("requestReviewFlow() > Error", it) }
            }
        }

    }

    /**
     * Update the preferences to indicate that the flow to rate app has just been shown.
     * @param context Context from which the function is called.
     * */
    private fun updatePreferencesOnFlowShown(context: Context) {

        logv("Invoked > updatePreferencesOnFlowShown()")

        val sharedPreferences = context.getSharedPreferences(Preferences.KEY, Context.MODE_PRIVATE)

        // The counter of times the flow has been shown is loaded, and it is increased by one, to count this time the flow was shown
        val flowShowCounter = sharedPreferences.getInt(Preferences.FLOW_SHOWN_COUNTER, 0) + 1

        with(sharedPreferences.edit()) {
            putInt(Preferences.LAUNCH_COUNTER, 0) // Restore launch counter
            putLong(Preferences.LAST_SHOW_DATE, Date().time)
            putInt(Preferences.FLOW_SHOWN_COUNTER, flowShowCounter)
            apply()
        }

        logv("updatePreferencesOnFlowShown() Done")

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Alternative utility to rate the app, it is in charge of directing the user to the app page on Google Play

    // Reference: https://stackoverflow.com/a/10816846
    /**
     * Directs the user to the app details on Google Play so they can rate the app. If is possible, open the details directly in
     * the Google Play app, if is not possible, open it in the browser, if it is not possible either, show a toast with a message.
     * @param activity Activity from which the process starts.
     * */
    fun goToRateOnGooglePlay(activity: Activity) {

        val marketUriString = "market://details?id=${activity.packageName}"
        val uri = Uri.parse(marketUriString)
        val googlePlayIntent = Intent(Intent.ACTION_VIEW, uri)
        googlePlayIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

        try {
            activity.startActivity(googlePlayIntent) // It tries to show in the Google Play app
            logv("User is sent to view app details in the google play app [$marketUriString]")
            logAnalyticsEvent(Event.RATE_APP_SENT_GOOGLE_PLAY)
        } catch (e1: ActivityNotFoundException) {
            try {
                // If it cannot be shown in the google play app, it tries to open in the default system web browser (It doesn't show a chooser)
                val webUriString = "https://play.google.com/store/apps/details?id=${activity.packageName}"
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webUriString)))
                logv("User is sent to view app details on google play on web browser [$webUriString]")
                logAnalyticsEvent(Event.RATE_APP_SENT_GOOGLE_PLAY)
            } catch (e2: ActivityNotFoundException) {
                // If it couldn't be displayed in either of the above two ways, show a toast
                activity.shortToast(R.string.rate_app_unable_to_show_app_on_google_play)
                logw("Unable to send the user to app details, google play app and web browser are not available", e2)
            }
        }

    }

}
