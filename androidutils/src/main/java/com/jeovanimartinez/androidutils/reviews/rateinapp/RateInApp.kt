package com.jeovanimartinez.androidutils.reviews.rateinapp

import android.content.Context
import com.jeovanimartinez.androidutils.Base

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
        const val LAUNCH_COUNTER = "rate_in_app_launch_counter"
        const val LAST_SHOW_DATE = "rate_in_app_last_show_date"
        const val NEVER_SHOW_AGAIN = "rate_in_app_never_show_again" // Aplica solo para el mensaje de versiones anteriores a Android 5
    }

    private var minInstallElapsedDays = 4

    /**
     * Establece el número mínimo de días requeridos desde que se instalo la app para poder mostrar el flujo, se usa en combinación con minInstallLaunchTimes,
     * y se deben cumplir ambas condiciones para mostrar el flujo
     * */
    fun setMinInstallElapsedDays(minInstallElapsedDays: Int): RateInApp {
        validateConfigArgument(minInstallElapsedDays)
        this.minInstallElapsedDays = minInstallElapsedDays
        return this
    }

    private var minInstallLaunchTimes = 5

    /**
     * Establece el número mínimo de veces que se debe haber iniciado la app desde que se instalo para poder mostrar el flujo, se usa en combinación con
     * minInstallElapsedDays, y se deben cumplir ambas condiciones para mostrar el flujo
     * */
    fun setMinInstallLaunchTimes(minInstallLaunchTimes: Int): RateInApp {
        validateConfigArgument(minInstallLaunchTimes)
        this.minInstallLaunchTimes = minInstallLaunchTimes
        return this
    }

    private var minRemindElapsedDays = 2

    /**
     * Número mínimo de días requeridos desde que se mostró el flujo para mostrarlo nuevamente, se usa en combinación con minRemindLaunchTimes,
     * y se deben cumplir ambas condiciones para mostrar el flujo
     * */
    fun setMinRemindElapsedDays(minRemindElapsedDays: Int): RateInApp {
        validateConfigArgument(minRemindElapsedDays)
        this.minRemindElapsedDays = minRemindElapsedDays
        return this
    }

    private var minRemindLaunchTimes = 3

    /**
     * Número mínimo de veces que se debe haber iniciado la app desde que mostró el flujo para mostrarlo nuevamente, se usa en combinación con
     * minRemindElapsedDays, y se deben cumplir ambas condiciones para mostrar el flujo
     * */
    fun setMinRemindLaunchTimes(minRemindLaunchTimes: Int): RateInApp {
        validateConfigArgument(minRemindLaunchTimes)
        this.minRemindLaunchTimes = minRemindLaunchTimes
        return this
    }

    /**
     * Inicializa y configura la utilidad, debe llamarse siempre solo una vez en la aplicación
     * ya sea en el onCreate() de la app o de la actividad principal
     * */
    fun init(context: Context) {

    }

    /** Se asegura que el argumento de configuración [value] sea válido */
    private fun validateConfigArgument(value: Int) {
        if (value < 0) throw IllegalArgumentException("Value must be equal to or greater than zero")
    }

}
