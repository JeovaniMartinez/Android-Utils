package com.jeovanimartinez.androidutils.reviews.rateinapp

import com.jeovanimartinez.androidutils.BuildConfig

/**
 * Utilidad para iniciar el flujo que invita al usuario a calificar la aplicación.
 * Para Android 5.0 (API 21) y posteriores, se utiliza Google Play In-App Review API, por lo que se puede calificar dentro de la aplicación.
 * Para versiones anteriores a Android 5.0, se muestra un diálogo para invitar al usuario a calificar la aplicación, si el usuario acepta, es
 * dirigido a los detalles de la aplicación en Google Play.
 * */
class RateInApp {

    companion object {

        /**
         * Número mínimo de días requeridos desde que se instalo la app para poder mostrar el flujo, se usa en combinación con minLaunchTimes,
         * y se deben cumplir ambas condiciones para mostrar el flujo
         * */
        var minInstallElapsedDays = 0

        /**
         * Número mínimo de veces que se ha iniciado la app desde que se instalo para poder mostrar el flujo, se usa en combinación con
         * minInstallElapsedDays, y se deben cumplir ambas condiciones para mostrar el flujo
         * */
        var minLaunchTimes = 0

        /** Para habilitar o deshabilitar los mensajes del log */
        var logEnable = BuildConfig.DEBUG

        /** Determina si ya se llamo al método build(), ya que es necesario antes de llamar cualquier otro método */
        private var builded = false

        /**
         * Método para configurar la utilidad, debe llamarse solo una vez al iniciarse la aplicación
         * */
        fun build() {

            builded = true
        }

    }

    /** Objeto con las constantes para las preferencias */
    private object Preferences {
        const val KEY = "rate_in_app_preferences"
        const val LAUNCH_COUNTER = "rate_in_app_launch_counter"
        const val LAST_SHOW_DATE = "rate_in_app_last_show_date"
        const val NEVER_SHOW_AGAIN = "rate_in_app_never_show_again"
    }

}
