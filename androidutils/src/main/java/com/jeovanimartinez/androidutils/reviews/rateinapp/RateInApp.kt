package com.jeovanimartinez.androidutils.reviews.rateinapp

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.android.play.core.review.ReviewManagerFactory
import com.jeovanimartinez.androidutils.Base
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Utilidad para iniciar el flujo que invita al usuario a calificar la aplicación, en base a ciertas condiciones sobre el uso de la aplicación.
 * Para Android 5.0 (API 21) y posteriores, se utiliza Google Play In-App Review API, por lo que se puede calificar dentro de la aplicación.
 * Para versiones anteriores a Android 5.0, se muestra un diálogo para invitar al usuario a calificar la aplicación, si el usuario acepta, es
 * dirigido a los detalles de la aplicación en Google Play.
 * */
@Suppress("unused")
object RateInApp : Base<RateInApp>() {

    override val LOG_TAG = "RateInApp"

    /** Objeto con las constantes para las preferencias */
    private object Preferences {
        const val KEY = "rate_in_app_preferences"
        const val CONFIGURED = "configured"
        const val LAUNCH_COUNTER = "rate_in_app_launch_counter"
        const val LAST_SHOW_DATE = "rate_in_app_last_show_date"
        const val FLOW_SHOWN_COUNTER = "flow_shown_counter"
        const val NEVER_SHOW_AGAIN = "rate_in_app_never_show_again" // Aplica solo para el mensaje de versiones anteriores a Android 5
    }

    private var minInstallElapsedDays = 4

    /**
     * Establece el número mínimo de días requeridos desde que se instalo la app para poder mostrar el flujo, se usa en combinación con minInstallLaunchTimes,
     * y se deben cumplir ambas condiciones para mostrar el flujo, el valor mínimo es 0 (se muestra a partir del dia en que se instalo)
     * */
    fun setMinInstallElapsedDays(minInstallElapsedDays: Int): RateInApp {
        validateConfigArgument(minInstallElapsedDays, 0)
        this.minInstallElapsedDays = minInstallElapsedDays
        return this
    }

    private var minInstallLaunchTimes = 5

    /**
     * Establece el número mínimo de veces que se debe haber iniciado la app desde que se instalo para poder mostrar el flujo, se usa en combinación con
     * minInstallElapsedDays, y se deben cumplir ambas condiciones para mostrar el flujo, el valor mínimo es 1, se muestra a partir del primer inicio
     * */
    fun setMinInstallLaunchTimes(minInstallLaunchTimes: Int): RateInApp {
        validateConfigArgument(minInstallLaunchTimes, 1)
        this.minInstallLaunchTimes = minInstallLaunchTimes
        return this
    }

    private var minRemindElapsedDays = 2

    /**
     * Número mínimo de días requeridos desde que se mostró el flujo para mostrarlo nuevamente, se usa en combinación con minRemindLaunchTimes,
     * y se deben cumplir ambas condiciones para mostrar el flujo, el valor mínimo es 0 (se muestra a partir de ese mismo dia)
     * */
    fun setMinRemindElapsedDays(minRemindElapsedDays: Int): RateInApp {
        validateConfigArgument(minRemindElapsedDays, 0)
        this.minRemindElapsedDays = minRemindElapsedDays
        return this
    }

    private var minRemindLaunchTimes = 3

    /**
     * Número mínimo de veces que se debe haber iniciado la app desde que mostró el flujo para mostrarlo nuevamente, se usa en combinación con
     * minRemindElapsedDays, y se deben cumplir ambas condiciones para mostrar el flujo, el valor mínimo es 1 (se muestra a partir del primer inicio)
     * */
    fun setMinRemindLaunchTimes(minRemindLaunchTimes: Int): RateInApp {
        validateConfigArgument(minRemindLaunchTimes, 1)
        this.minRemindLaunchTimes = minRemindLaunchTimes
        return this
    }

    private var showAtEvent = 2

    /**
     * Cuando se llama a checkAndShow() se va a mostrar el flujo si se cumplen las condiciones, setShowAtEvent permite modificar a las cuantas veces
     * que se llama a checkAndShow() se muestre el flujo. Por ejemplo, se desea mostrar el flujo para calificar en el onResume() de la actividad principal,
     * pero la tercer vez que se llame a ese método, en ese caso, establecer showAtEvent = 3, que hará que se muestre el flujo hasta la tercera vez que se llame
     * a onResume(). Si de otro modo, showAtEvent fuera 1, el flujo se mostraría en la primera llamada a onResume(). El valor mínimo de showAtEvent es 1
     * */
    fun setShowAtEvent(showAtEvent: Int): RateInApp {
        validateConfigArgument(showAtEvent, 1)
        this.showAtEvent = showAtEvent
        return this
    }

    /** Se asegura que el argumento de configuración [value] sea válido */
    private fun validateConfigArgument(value: Int, minValue: Int) {
        if (value < minValue) throw IllegalArgumentException("Invalid config value, value ($value) must be equal to or greater than $minValue")
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
            log("RateInApp is already initialized")
            return
        }
        configurePreferences(context) // Se configuran las preferencias
        updateLaunchCounter() // Se cuenta el nuevo inicio
        initialized = true
        log(
            """
            RateInApp initialized
            minInstallElapsedDays: $minInstallElapsedDays
            minInstallLaunchTimes: $minInstallLaunchTimes
            minRemindElapsedDays: $minRemindElapsedDays
            minRemindLaunchTimes: $minRemindLaunchTimes
            showAtEvent: $showAtEvent
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
     * Ejecuta la verificación y muestra el flujo para calificar, llamar cuando se valide que se cumplen todas las condiciones de la configuración
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
        if (launchCounter < minLaunchTimes) {
            log("It is not necessary to show flow to rate app, a minimum of $minLaunchTimes launches is required, current: $launchCounter")
            return // No es necesario continuar, no se cumple una condición requerida
        }

        // Se cumplen los inicios requeridos
        log("The condition of minimum required launches is met, current: $launchCounter | required: $minLaunchTimes")

        // Se calculan los dias transcurridos entre la última fecha que se mostró el mensaje y la fecha actual
        val elapsedDays = ((Date().time - lastShowDateValue) / TimeUnit.DAYS.toMillis(1)).toInt()
        log("Elapsed days between last date of flow to rate app showed and today is: $elapsedDays")

        // Si los días transcurridos son negativos, indica una alteración en la fecha del dispositivo, por lo que el valor de LAST_SHOW_DATE se restablece
        if (elapsedDays < 0) {
            sharedPreferences.edit().putLong(Preferences.LAST_SHOW_DATE, Date().time).apply()
            log("Elapsed days ($elapsedDays) value is negative and invalid, the value is restarted to current date")
            return // No es necesario continuar, ya que se restableció la fecha
        }

        // Se verifica si han transcurrido los días mínimos requeridos
        if (elapsedDays < minElapsedDays) {
            log("It is not necessary to show flow to rate app, a minimum of $minElapsedDays days elapsed is required, current: $elapsedDays")
            return // No es necesario continuar, no se cumple una condición requerida
        }

        // Se cumplen los días transcurridos
        log("The condition of minimum elapsed days is met, current: $elapsedDays | required: $minElapsedDays")

        // Se cumplen todas las condiciones, se debe mostrar el flujo para calificar la aplicación
        log("All conditions are met, the flow to rate app must be shown")

        log("The SDK version currently running is: ${android.os.Build.VERSION.SDK_INT}")

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Para Android 5 y posteriores, donde hay soporte para Google Play In-App Review API
            log("Let's go to rate with Google Play In-App Review API")
            rateWithInAppReviewApi(activity)
        } else {
            // Para versiones anteriores a Android 5, donde Google Play In-App Review API no esta disponible
            log("Let's go to rate with Dialog")
            rateWithDialog(activity)
        }

    }

    /**
     * Muestra el flujo para calificar con Google Play In-App Review API.
     * Referencia: https://developer.android.com/guide/playcore/in-app-review
     *
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
                log("ReviewFlow successfully obtained")
                firebaseAnalytics?.logEvent("rate_app_review_flow_obtained", null)
                val reviewInfo = request.result // Se obtiene el resultado
                val reviewFlow = reviewManager.launchReviewFlow(activity, reviewInfo) // Se lanza el flujo para calificar
                reviewFlow.addOnCompleteListener {
                    /*
                    * Al finalizar el flujo, teniendo en cuenta que la API no informa si el usuario califico la app o no,
                    * tampoco informa si se mostró el diálogo o no, por lo que solo indica el fin del proceso.
                    *
                    * Consideraciones:
                    *   - No se puede saber si se mostró el flujo o no, ni el resultado (si se califico o no la app)
                    *   - Si el usuario ya ha calificado la aplicación, el flujo no se muestra, pero se llama aquí reviewFlow.addOnCompleteListener
                    *   - Si el usuario aun no califica la app, pero ya se supero la cuota para mostrar el mensaje, el mensaje no se muestra
                    * */
                    log("Finished flow to rate app with Google Play In-App Review API")
                    firebaseAnalytics?.logEvent("rate_app_review_flow_completed", null)
                }
            } else {
                validated = false // Se regresa a false, para intentarlo nuevamente en esta sesión, ya que no se pudo mostrar el flujo
                log("Error on request ReviewFlow, can not show flow to rate app")
                firebaseAnalytics?.logEvent("rate_app_request_review_flow_error", null)
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
        firebaseAnalytics?.logEvent("rate_app_dialog_shown", null)

        log("CALIFICAR DIALOGO PENDIENTE...")
    }

}
