package com.jeovanimartinez.androidutils.reviews

import android.app.Activity
import android.app.ActivityOptions
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import com.google.android.play.core.review.ReviewManagerFactory
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.extensions.context.shortToast
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Set of utilities to invite the user to rate the app.
 * */
object RateApp : Base<RateApp>() {

    override val LOG_TAG = "RateApp"

    // Time elapsed from when the flow was obtained until it was completed to consider that the flow was displayed
    private const val RATE_FLOW_MIN_ELAPSED_TIME = 2000 // In milliseconds

    /** Object with constants for the preferences. */
    private object Preferences {
        const val KEY = "rate_in_app_preferences"
        const val CONFIGURED = "configured"
        const val LAUNCH_COUNTER = "rate_in_app_launch_counter"
        const val LAST_SHOW_DATE = "rate_in_app_last_show_date"
        const val FLOW_SHOWN_COUNTER = "flow_shown_counter"
        const val NEVER_SHOW_AGAIN = "rate_in_app_never_show_again" // Applies only for the rate dialog of versions prior to Android 5
    }

    /**
     * Minimum number of days required since the app was installed to be able to show the flow, it is used in combination with minInstallLaunchTimes,
     * and both conditions must be fulfilled to show the flow, the minimum value is 0 (it is shown from that same day).
     * */
    var minInstallElapsedDays = 10
        set(value) {
            field = validateConfigArgument(value, 0)
        }

    /**
     * Minimum number of times the app must have been launched since it was installed to be able to show the flow, it is used in combination with
     * minInstallElapsedDays, and both conditions must be fulfilled to show the flow, the minimum value is 1 (shown from first launch).
     * */
    var minInstallLaunchTimes = 10
        set(value) {
            field = validateConfigArgument(value, 1)
        }

    /**
     * Minimum number of days required since the flow was shown to show it again, it is used in combination with minRemindLaunchTimes, and both
     * conditions must be fulfilled to show the flow, the minimum value is 0 (it is shown from that same day).
     * */
    var minRemindElapsedDays = 2
        set(value) {
            field = validateConfigArgument(value, 0)
        }

    /**
     * Minimum number of times the app must have been launched since it showed the flow to show it again, it is used in combination with
     * minRemindElapsedDays, and both conditions must be fulfilled to show the flow, the minimum value is 1 (it is shown from the first launch).
     * */
    var minRemindLaunchTimes = 4
        set(value) {
            field = validateConfigArgument(value, 1)
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
            field = validateConfigArgument(value, 1)
        }

    /**
     * For versions prior to Android 5, where the dialog to invite the user to rate app is showing, it allows setting the visibility of
     * the never ask again button.
     * */
    var showNeverAskAgainButton = true


    /** Ensures that the config argument [value] is valid and returns the same value for easier assignment. */
    private fun validateConfigArgument(value: Int, minValue: Int): Int {
        if (value < minValue) throw IllegalArgumentException("Invalid config value, value ($value) must be equal to or greater than $minValue")
        return value
    }

    private lateinit var sharedPreferences: SharedPreferences // To manipulate preferences
    private var initialized = false // Helper to determine if init was already called
    private var eventCount = 0 // Helper to count how many times checkAndShow() has been called
    private var validated = false // Determines if it has already been validated if the flow should be shown, to only validate it once per session.

    /**
     * Initialize and configure the utility, it must always be called only once from the app.
     * @param context Context.
     * */
    fun init(context: Context) {
        if (initialized) {
            log("RateApp is already initialized")
            return
        }

        configurePreferences(context)
        updateLaunchCounter()
        initialized = true

        log(
            """
            RateApp initialized
            minInstallElapsedDays: $minInstallElapsedDays
            minInstallLaunchTimes: $minInstallLaunchTimes
            minRemindElapsedDays: $minRemindElapsedDays
            minRemindLaunchTimes: $minRemindLaunchTimes
            showAtEvent: $showAtEvent
            showNeverAskAgainButton: $showNeverAskAgainButton
        """.trimIndent()
        )
    }

    /**
     * Checks if the all conditions to show the flow are fulfilled, and shows the flow only if the all conditions are fulfilled.
     * @param activity Activity.
     * */
    fun checkAndShow(activity: Activity) {
        if (!initialized) throw Exception("Need call init() before call this method") // It's necessary to call init before calling this method

        if (validated) return log("The conditions have already been validated in this session")

        eventCount++

        // If it does not apply to show in this call to the event
        if (showAtEvent != eventCount) return log("No need verify conditions in this call, showAtEvent: $showAtEvent | eventCount $eventCount")

        log("We proceed to verify the conditions to show the flow to rate the app, event number: $showAtEvent")
        doCheckAndShow(activity) // Execute the full verification

        validated = true // At the end, it is indicated that it is already valid, to only do it once per session
    }

    /**
     * Generate the instance to manipulate the preferences and set the preferences to the default values in case they are not already configured.
     * @param context Context.
     * */
    private fun configurePreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences(Preferences.KEY, Context.MODE_PRIVATE)
        if (!sharedPreferences.getBoolean(Preferences.CONFIGURED, false)) {
            with(sharedPreferences.edit()) {
                putInt(Preferences.LAUNCH_COUNTER, 0)
                putLong(Preferences.LAST_SHOW_DATE, Date().time) // In first time, it is set with the current date, to have a reference value
                putInt(Preferences.FLOW_SHOWN_COUNTER, 0)
                putBoolean(Preferences.NEVER_SHOW_AGAIN, false)
                putBoolean(Preferences.CONFIGURED, true)
                apply()
            }
            log("The preferences are configured for first time")
        } else {
            log("The preferences are already configured")
        }
    }

    /** Update the app launch counter. */
    private fun updateLaunchCounter() {
        val currentValue = sharedPreferences.getInt(Preferences.LAUNCH_COUNTER, 0)
        val newValue = currentValue + 1
        sharedPreferences.edit().putInt(Preferences.LAUNCH_COUNTER, newValue).apply()
        log("Updated launch counter value from $currentValue to $newValue")
    }

    /**
     * Execute the verification and show the flow to rate the app, call after executing the first checks in checkAndShow(),
     * the functions were separated for better code structure.
     * @param activity Activity.
     * */
    private fun doCheckAndShow(activity: Activity) {

        log("doCheckAndShow() Invoked")

        // First, preferences for reference values are loaded
        val launchCounter = sharedPreferences.getInt(Preferences.LAUNCH_COUNTER, 1)
        val lastShowDateValue = sharedPreferences.getLong(Preferences.LAST_SHOW_DATE, 0)
        val lastShowDate = Date(lastShowDateValue)
        val flowShowCounter = sharedPreferences.getInt(Preferences.FLOW_SHOWN_COUNTER, 0)
        val neverShowAgain = sharedPreferences.getBoolean(Preferences.NEVER_SHOW_AGAIN, false)

        val minElapsedDays: Int
        val minLaunchTimes: Int

        if (flowShowCounter == 0) {
            // If the flow has never shown, the values are assigned according to the installation values
            minElapsedDays = minInstallElapsedDays
            minLaunchTimes = minInstallLaunchTimes
            log("Values configured by install values")
        } else {
            // If the flow has already been shown at least once, the values are assigned based on the reminder values
            minElapsedDays = minRemindElapsedDays
            minLaunchTimes = minRemindLaunchTimes
            log("Values configured by remind values")
        }

        // Show values for debugging purposes, date is displayed in local format for better understanding
        log(
            """
            Current Values
            launchCounter: $launchCounter
            lastShowDateValue $lastShowDateValue
            lastShowDate: ${DateFormat.getDateTimeInstance().format(lastShowDate)}
            flowShowCounter: $flowShowCounter
            neverShowAgain: $neverShowAgain
            minElapsedDays: $minElapsedDays
            minLaunchTimes: $minLaunchTimes
        """.trimIndent()
        )

        // Check app launches
        val launchCounterFulfilled = if (launchCounter < minLaunchTimes) {
            log("The condition of minimum required launches is not fulfilled, a minimum of $minLaunchTimes launches is required, current: $launchCounter")
            false
        } else {
            log("The condition of minimum required launches is fulfilled, current: $launchCounter | required: $minLaunchTimes")
            true
        }

        // Calculate elapsed days between the last date the flow was shown and the current date
        val elapsedDays = ((Date().time - lastShowDateValue) / TimeUnit.DAYS.toMillis(1)).toInt()
        log("Elapsed days between last date of flow to rate app showed and today is: $elapsedDays")

        var elapsedDaysFulfilled = false // Initial value

        // If the elapsed days are negative, it indicates an alteration in the date of the device, so the value of LAST_SHOW_DATE is reset
        if (elapsedDays < 0) {
            sharedPreferences.edit().putLong(Preferences.LAST_SHOW_DATE, Date().time).apply()
            log("Elapsed days ($elapsedDays) value is negative and invalid, the value is restarted to current date")
        } else {
            // Check elapsed days
            elapsedDaysFulfilled = if (elapsedDays < minElapsedDays) {
                log("The condition of minimum elapsed days is not fulfilled, a minimum of $minElapsedDays days elapsed is required, current: $elapsedDays")
                false
            } else {
                log("The condition of minimum elapsed days is fulfilled, current: $elapsedDays | required: $minElapsedDays")
                true
            }
        }

        // If any of the conditions are not fulfilled
        if (!launchCounterFulfilled || !elapsedDaysFulfilled) {
            log("Not all conditions are fulfilled [launchCounterFulfilled = $launchCounterFulfilled] [elapsedDaysFulfilled = $elapsedDaysFulfilled], " +
                         "It is not necessary to show flow to rate app")
            return
        }

        // If all conditions are fulfilled, the flow to rate app must be shown

        log("All conditions are fulfilled, the flow to rate app must be shown")

        log("The SDK version currently running is: ${Build.VERSION.SDK_INT}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // For Android 5 and later, where there is support for Google Play In-App Review API
            log("Let's go to rate with Google Play In-App Review API")
            rateWithInAppReviewApi(activity)
        } else {
            log("Let's go to rate with Dialog")
            rateWithDialog(activity)
        }

    }

    // Referencia: https://developer.android.com/guide/playcore/in-app-review
    /**
     * Muestra el flujo para calificar con Google Play In-App Review API.
     * @param activity actividad
     * */
    private fun rateWithInAppReviewApi(activity: Activity) {
        log("rateWithInAppReviewApi() Invoked")
        val reviewManager = ReviewManagerFactory.create(activity)
        val managerRequest = reviewManager.requestReviewFlow()
        // Primero hay que crear la solicitud
        managerRequest.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                // Si la solicitud se creo correctamente
                var successfulReviewFlow = false // Determina si el flujo fue correcto
                val flowObtainedTime = Date().time // Se almacena la fecha en que se obtuvo el flujo
                log("ReviewFlow successfully obtained")
                val reviewInfo = request.result // Se obtiene el resultado
                val reviewFlow = reviewManager.launchReviewFlow(activity, reviewInfo) // Se lanza el flujo para calificar
                // El proceso del flujo fue exitoso
                reviewFlow.addOnSuccessListener {
                    /*
                     * El flujo fue correcto, teniendo en cuenta que la API no informa si el usuario califico la app o no,
                     * tampoco informa si se mostró el diálogo o no, por lo que solo indica que el proceso fue exitoso.
                     *
                     * Consideraciones:
                     *   - No se puede saber si se mostró el flujo o no, ni el resultado (si se califico o no la app)
                     *   - Si el usuario ya ha calificado la aplicación, el flujo no se muestra, pero se llama aquí
                     *   - Si el usuario aun no califica la app, pero ya se supero la cuota para mostrar el mensaje, el mensaje no se muestra
                     */
                    successfulReviewFlow = true // El flujo fue correcto
                    log("Successful review flow to rate app with Google Play In-App Review API")
                    updatePreferencesOnFlowShown() // Se Actualizan las preferencias, ya que se completo el flujo
                    firebaseAnalytics("rate_app_review_flow_successful", null)
                }
                // Error en el flujo
                reviewFlow.addOnFailureListener {
                    validated = false // Se regresa a false, para intentarlo nuevamente en esta sesión, ya que no se pudo mostrar el flujo
                    log("Failure on ReviewFlow, can not show flow to rate app")
                    firebaseAnalytics("rate_app_review_flow_failure", null)
                }
                // Flujo completado
                reviewFlow.addOnCompleteListener {
                    log("Finished flow to rate app with Google Play In-App Review API")
                    /*
                    * Si el el resultado del flujo fue correcto.
                    * La API no informa si se mostró el flujo o no, pero según pruebas realizadas, esto se puede inferir determinando el tiempo desde que se
                    * obtuvo el flujo hasta que se completo, si tarda un determinado tiempo, lo más probable es que el flujo se haya mostrado, no es una certeza
                    * del 100%, pero se infiere por el tiempo transcurrido.
                    * Registrar si se mostró o no el flujo es muy útil para los eventos de analytics, ya que permite ajustar la configuración de cada cuantos días
                    * y cada cuantos inicios de la app es conveniente intentar mostrar el flujo para obtener el mayor número de revisiones en la app sin molestar
                    * mucho al usuario.
                    * */
                    if (successfulReviewFlow) {
                        val elapsedTime = Date().time - flowObtainedTime
                        log("Elapsed time in review flow ${elapsedTime / 1000.0} seconds ($elapsedTime milliseconds)")
                        if (elapsedTime >= RATE_FLOW_MIN_ELAPSED_TIME) {
                            log("Elapsed time ${elapsedTime / 1000.0} is greater or equal to ${RATE_FLOW_MIN_ELAPSED_TIME / 1000.0}, it is considered that the flow was shown to user")
                            firebaseAnalytics("rate_app_review_flow_showed", null)
                        }
                    }
                }
            } else {
                validated = false // Se regresa a false, para intentarlo nuevamente en esta sesión, ya que no se pudo mostrar el flujo
                log("Error on request ReviewFlow, can not show flow to rate app")
                firebaseAnalytics("rate_app_request_review_flow_error", null)
            }
        }
    }

    /**
     * Muestra un mensaje para invitar al usuario a calificar la app, en caso de confirmación, el usuario
     * es dirigido a los detalles de la app en Google Play para que pueda calificarla
     *
     * @param activity actividad
     * */
    private fun rateWithDialog(activity: Activity) {
        log("rateWithDialog() Invoked")

        // Se verifica si aún hay que mostrar el diálogo
        val neverShowAgain = sharedPreferences.getBoolean(Preferences.NEVER_SHOW_AGAIN, false)

        if (!neverShowAgain) {
            // Hay que mostrar el diálogo
            log("neverShowAgain = $neverShowAgain | The dialogue is shown")
            updatePreferencesOnFlowShown() // Se actualizan las preferencias, ya que se muestro el diálogo
            firebaseAnalytics("rate_app_dialog_shown", null)

            // Se muestra la actividad (estilo diálogo)
            activity.startActivity(
                Intent(activity, RateAppActivity::class.java),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ActivityOptions.makeSceneTransitionAnimation(activity).toBundle() else null
            )

        } else {
            // El usuario ya había indicado que no quiere ver el diálogo
            log("neverShowAgain = $neverShowAgain | No need to show dialogue anymore")
        }

    }

    /**
     * Actualiza las preferencias para indicar que se acaba de mostrar el flujo para calificar la aplicación,
     * llamar solo en el reviewFlow.addOnCompleteListener (que indica que se completo el flujo) o llamar cuando
     * se muestre el diálogo para calificar en versiones anteriores a Android 5
     * */
    private fun updatePreferencesOnFlowShown() {

        log("updatePreferencesOnFlowShown() Invoked")

        // Se carga el contador de veces que se ha mostrado el flujo, y se le incrementa uno, para contar esta vez que se mostró el flujo
        val flowShowCounter = sharedPreferences.getInt(Preferences.FLOW_SHOWN_COUNTER, 0) + 1

        with(sharedPreferences.edit()) {
            putInt(Preferences.LAUNCH_COUNTER, 0) // Se restablece el contador de inicios a cero
            putLong(Preferences.LAST_SHOW_DATE, Date().time) // Se actualiza la fecha en que se mostró el flujo
            putInt(Preferences.FLOW_SHOWN_COUNTER, flowShowCounter) // Se actualiza el contador de veces que se ha mostrado el flujo
            apply()
        }

        log("updatePreferencesOnFlowShown() Done")

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Utilidad alternativa para calificar la app, se encarga de dirigir al usuario a la página de la app en Google Play

    // Referencia: https://stackoverflow.com/a/10816846
    /**
     * Dirige al usuario a los detalles de la aplicación en Google Play para que pueda calificar la aplicación.
     * Si es posible, se abre la aplicación directamente en la app de Google Play, en caso de no ser posible, se abre
     * en el navegador, si tampoco es posible, muestra un toast con un mensaje.
     *
     * @param activity actividad desde donde se llama, se usa para iniciar otras actividades
     * */
    fun goToRateInGooglePlay(activity: Activity) {
        val marketUriString = "market://details?id=${activity.packageName}"
        val uri = Uri.parse(marketUriString)
        val googlePlayIntent = Intent(Intent.ACTION_VIEW, uri)
        googlePlayIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            activity.startActivity(googlePlayIntent) // Se intenta mostrar en la app de google play
            log("Sent user to view app details in google play app [$marketUriString]")
            firebaseAnalytics("rate_app_sent_to_google_play_app", null)
        } catch (e1: ActivityNotFoundException) {
            try {
                // Si no se puede mostrar en la app de google play, se intenta abrir en el navegador web
                val webUriString = "http://play.google.com/store/apps/details?id=${activity.packageName}"
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webUriString)))
                log("Sent user to view app details in google play on web browser [$webUriString]")
                firebaseAnalytics("rate_app_sent_to_google_play_web", null)
            } catch (e2: ActivityNotFoundException) {
                // Si no se pudo mostrar en ninguna de las dos maneras anteriores, muestra un mensaje
                activity.shortToast(R.string.rate_app_unable_to_show_app_on_google_play)
                logw("Unable to send user to app details, google play app and web browser are not available", e2)
                firebaseAnalytics("rate_app_unable_to_show_on_google_play", null)
            }
        }
    }

}
