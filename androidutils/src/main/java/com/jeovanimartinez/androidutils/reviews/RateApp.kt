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
 * Utilidad para iniciar el flujo que invita al usuario a calificar la aplicación, en base a ciertas condiciones sobre el uso de la aplicación.
 * Para Android 5.0 (API 21) y posteriores, se utiliza Google Play In-App Review API, por lo que se puede calificar dentro de la aplicación.
 * Para versiones anteriores a Android 5.0, se muestra un diálogo para invitar al usuario a calificar la aplicación, si el usuario acepta, es
 * dirigido a los detalles de la aplicación en Google Play.
 * */
object RateApp : Base<RateApp>() {

    override val LOG_TAG = "RateApp"

    // Tiempo transcurrido desde que se obtuvo el flujo hasta que se completo para considerar que se mostró el flujo
    private const val RATE_FLOW_MIN_ELAPSED_TIME = 2000 // Expresado en milisegundos

    /** Objeto con las constantes para las preferencias */
    private object Preferences {
        const val KEY = "rate_in_app_preferences"
        const val CONFIGURED = "configured"
        const val LAUNCH_COUNTER = "rate_in_app_launch_counter"
        const val LAST_SHOW_DATE = "rate_in_app_last_show_date"
        const val FLOW_SHOWN_COUNTER = "flow_shown_counter"
        const val NEVER_SHOW_AGAIN = "rate_in_app_never_show_again" // Aplica solo para el mensaje de versiones anteriores a Android 5
    }

    /**
     * Número mínimo de días requeridos desde que se instalo la app para poder mostrar el flujo, se usa en combinación con minInstallLaunchTimes,
     * y se deben cumplir ambas condiciones para mostrar el flujo, el valor mínimo es 0 (se muestra a partir de ese mismo dia)
     * */
    var minInstallElapsedDays = 10
        set(value) {
            field = validateConfigArgument(value, 0)
        }

    /**
     * Número mínimo de veces que se debe haber iniciado la app desde que se instalo para poder mostrar el flujo, se usa en combinación con minInstallElapsedDays,
     * y se deben cumplir ambas condiciones para mostrar el flujo, el valor mínimo es 1 (se muestra a partir del primer inicio)
     * */
    var minInstallLaunchTimes = 10
        set(value) {
            field = validateConfigArgument(value, 1)
        }

    /**
     * Número mínimo de días requeridos desde que se mostró el flujo para mostrarlo nuevamente, se usa en combinación con minRemindLaunchTimes,
     * y se deben cumplir ambas condiciones para mostrar el flujo, el valor mínimo es 0 (se muestra a partir de ese mismo dia)
     * */
    var minRemindElapsedDays = 2
        set(value) {
            field = validateConfigArgument(value, 0)
        }

    /**
     * Número mínimo de veces que se debe haber iniciado la app desde que mostró el flujo para mostrarlo nuevamente, se usa en combinación con
     * minRemindElapsedDays, y se deben cumplir ambas condiciones para mostrar el flujo, el valor mínimo es 1 (se muestra a partir del primer inicio)
     * */
    var minRemindLaunchTimes = 4
        set(value) {
            field = validateConfigArgument(value, 1)
        }

    /**
     * Cuando se llama a checkAndShow() se va a mostrar el flujo si se cumplen las condiciones, setShowAtEvent permite modificar a las cuantas veces
     * que se llama a checkAndShow() se muestre el flujo. Por ejemplo, se desea mostrar el flujo para calificar en el onResume() de la actividad principal,
     * pero la tercer vez que se llame a ese método, en ese caso, establecer showAtEvent = 3, que hará que se muestre el flujo hasta la tercera vez que se llame
     * a onResume(). Si de otro modo, showAtEvent fuera 1, el flujo se mostraría en la primera llamada a onResume(). El valor mínimo de showAtEvent es 1
     * */
    var showAtEvent = 2
        set(value) {
            field = validateConfigArgument(value, 1)
        }

    /**
     * Para versiones anteriores a Android 5, donde se muestra el diálogo para invitar al usuario a calificar la app,
     * permite establecer la visibilidad del botón de nunca volver a preguntar, en base a [showNeverAskAgainButton]
     * */
    var showNeverAskAgainButton = true // De manera predeterminada si se muestra el botón


    /** Se asegura que el argumento de configuración [value] sea válido y devuelve el mismo valor para una asignación mas fácil */
    private fun validateConfigArgument(value: Int, minValue: Int): Int {
        if (value < minValue) throw IllegalArgumentException("Invalid config value, value ($value) must be equal to or greater than $minValue")
        return value
    }

    private lateinit var sharedPreferences: SharedPreferences // Para manipular las preferencias
    private var initialized = false // Auxiliar para determinar si ya se llamo a init
    private var eventCount = 0 // Auxiliar para contar cuantas veces se ha llamado a checkAndShow
    private var validated = false // Determina si ya se valido si se debe mostrar el flujo, para solo validarlo una vez por sesión

    /**
     * Inicializa y configura la utilidad, debe llamarse siempre solo una vez en la aplicación
     * ya sea en el onCreate() de la app o de la actividad principal
     * @param context contexto
     * */
    fun init(context: Context) {
        if (initialized) {
            log("RateApp is already initialized")
            return
        }
        configurePreferences(context) // Se configuran las preferencias
        updateLaunchCounter() // Se cuenta el nuevo inicio
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
     * Verifica si se cumplen las condiciones para mostrar el flujo para calificar, y muestra el flujo solo si se cumplen las condiciones
     * @param activity actividad desde donde se llama
     * */
    fun checkAndShow(activity: Activity) {
        if (!initialized) throw Exception("Need call init() before call this method") // Es necesario llamar a init antes de llamar a este método

        if (validated) return log("The conditions have already been validated in this session") // Solo se valida una vez por sesión

        eventCount++ // Se actualiza el contador de veces que se llama a este método

        // Si no corresponde mostrar en esta llamada al evento
        if (showAtEvent != eventCount) return log("No need verify conditions in this call, showAtEvent: $showAtEvent | eventCount $eventCount")

        // Al llegar a este punto, se debe validar si se va a mostrar el flujo
        log("We proceed to verify the conditions to show the flow to rate the app, event number: $showAtEvent")
        doCheckAndShow(activity) // Se ejecuta la verificación

        validated = true // Al final, se indica que ya se valido, para solo hacerlo una vez por sesión
    }

    /**
     * Genera la instancia para manipular las preferencias y configura las preferencias a los valores predeterminados en caso de que aún no estén configuradas
     * @param context contexto
     * */
    private fun configurePreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences(Preferences.KEY, Context.MODE_PRIVATE) // Se crea la instancia del objeto para manipular las preferencias
        if (!sharedPreferences.getBoolean(Preferences.CONFIGURED, false)) {
            with(sharedPreferences.edit()) {
                putInt(Preferences.LAUNCH_COUNTER, 0)
                putLong(Preferences.LAST_SHOW_DATE, Date().time) // Al inicio, se configura con la fecha actual, para tener un valor de referencia
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

    /** Actualiza el contador de inicios de la app */
    private fun updateLaunchCounter() {
        val currentValue = sharedPreferences.getInt(Preferences.LAUNCH_COUNTER, 0)
        val newValue = currentValue + 1
        sharedPreferences.edit().putInt(Preferences.LAUNCH_COUNTER, newValue).apply()
        log("Updated launch counter value from $currentValue to $newValue")
    }

    /**
     * Ejecuta la verificación y muestra el flujo para calificar, llamar cuando se valide que se cumplen todas las condiciones de la configuración,
     * llamar después de ejecutar las primeras verificaciones en CheckAndShow, las funciones se separaron para mejor estructura del código
     * @param activity actividad
     * */
    private fun doCheckAndShow(activity: Activity) {

        log("doCheckAndShow() Invoked")

        // Primero se carga el valor de cuantas veces se ha mostrado el flujo para calificar
        val launchCounter = sharedPreferences.getInt(Preferences.LAUNCH_COUNTER, 1)
        val lastShowDateValue = sharedPreferences.getLong(Preferences.LAST_SHOW_DATE, 0)
        val lastShowDate = Date(lastShowDateValue)
        val flowShowCounter = sharedPreferences.getInt(Preferences.FLOW_SHOWN_COUNTER, 0)
        val neverShowAgain = sharedPreferences.getBoolean(Preferences.NEVER_SHOW_AGAIN, false)

        val minElapsedDays: Int
        val minLaunchTimes: Int

        if (flowShowCounter == 0) {
            // Si el flujo se no se ha mostrado ninguna vez, se asignan los valores según los valores de instalación
            minElapsedDays = minInstallElapsedDays
            minLaunchTimes = minInstallLaunchTimes
            log("Values configured by install values")
        } else {
            // Si el flujo ya se ha mostrado al menos una vez, se asignan los valores según los valores de recordatorio
            minElapsedDays = minRemindElapsedDays
            minLaunchTimes = minRemindLaunchTimes
            log("Values configured by remind values")
        }

        // Se muestran los valores con fines de depuración, la fecha se muestra en formato local para su mejor comprensión
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

        // Se verifica si se cumplen los inicios mínimos requeridos
        val launchCounterFulfilled = if (launchCounter < minLaunchTimes) {
            log("The condition of minimum required launches is not fulfilled, a minimum of $minLaunchTimes launches is required, current: $launchCounter")
            false
        } else {
            // Se cumplen los inicios requeridos
            log("The condition of minimum required launches is fulfilled, current: $launchCounter | required: $minLaunchTimes")
            true
        }

        // Se calculan los dias transcurridos entre la última fecha que se mostró el mensaje y la fecha actual
        val elapsedDays = ((Date().time - lastShowDateValue) / TimeUnit.DAYS.toMillis(1)).toInt()
        log("Elapsed days between last date of flow to rate app showed and today is: $elapsedDays")

        var elapsedDaysFulfilled = false // Para determinar si se cumple la condición de días transcurridos

        // Si los días transcurridos son negativos, indica una alteración en la fecha del dispositivo, por lo que el valor de LAST_SHOW_DATE se restablece
        if (elapsedDays < 0) {
            sharedPreferences.edit().putLong(Preferences.LAST_SHOW_DATE, Date().time).apply()
            log("Elapsed days ($elapsedDays) value is negative and invalid, the value is restarted to current date")
        } else {
            // Se verifica si han transcurrido los días mínimos requeridos
            elapsedDaysFulfilled = if (elapsedDays < minElapsedDays) {
                log("The condition of minimum elapsed days is not fulfilled, a minimum of $minElapsedDays days elapsed is required, current: $elapsedDays")
                false
            } else {
                // Se cumplen los días transcurridos
                log("The condition of minimum elapsed days is fulfilled, current: $elapsedDays | required: $minElapsedDays")
                true
            }
        }

        // Si alguna de las condiciones no se cumplen
        if (!launchCounterFulfilled || !elapsedDaysFulfilled) {
            log("Not all conditions are fulfilled [launchCounterFulfilled = $launchCounterFulfilled] [elapsedDaysFulfilled = $elapsedDaysFulfilled], It is not necessary to show flow to rate app")
            return
        }

        // Si se cumplen todas las condiciones, se debe mostrar el flujo para calificar la aplicación

        log("All conditions are fulfilled, the flow to rate app must be shown")

        log("The SDK version currently running is: ${Build.VERSION.SDK_INT}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Para Android 5 y posteriores, donde hay soporte para Google Play In-App Review API
            log("Let's go to rate with Google Play In-App Review API")
            rateWithInAppReviewApi(activity)
        } else {
            // Para versiones anteriores a Android 5, donde Google Play In-App Review API no esta disponible
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
