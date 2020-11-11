package com.jeovanimartinez.androidutils.reviews.rateinapp

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.jeovanimartinez.androidutils.Base
import java.util.*

/**
 * Utilidad para iniciar el flujo que invita al usuario a calificar la aplicación, en base a ciertas condiciones sobre el uso de la aplicación.
 * Para Android 5.0 (API 21) y posteriores, se utiliza Google Play In-App Review API, por lo que se puede calificar dentro de la aplicación.
 * Para versiones anteriores a Android 5.0, se muestra un diálogo para invitar al usuario a calificar la aplicación, si el usuario acepta, es
 * dirigido a los detalles de la aplicación en Google Play.
 * */
object RateInApp : Base<RateInApp>() {

    override val LOG_TAG = "RateInApp"

    /** Objeto con las constantes para las preferencias */
    object Preferences {
        const val KEY = "rate_in_app_preferences"
        const val CONFIGURED = "configured"
        const val FIRST_OPEN_DATE = "first_open_date"
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
        if (!initialized) throw Exception("Need call init() before call this method")
    }

    /**
     * Genera la instancia para manipular las preferencias y configura las preferencias a los valores predeterminados en caso de que aún no estén configuradas
     * @param context contexto
     * */
    private fun configurePreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences(Preferences.KEY, Context.MODE_PRIVATE) // Se crea la instancia del objeto para manipular las preferencias
        if (!sharedPreferences.getBoolean(Preferences.CONFIGURED, false)) {
            with(sharedPreferences.edit()) {
                putLong(Preferences.FIRST_OPEN_DATE, Date().time)
                putInt(Preferences.LAUNCH_COUNTER, 0)
                putLong(Preferences.LAST_SHOW_DATE, 0)
                putLong(Preferences.FLOW_SHOWN_COUNTER, 0)
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

}
