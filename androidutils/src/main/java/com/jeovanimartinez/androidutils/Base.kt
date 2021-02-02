@file:Suppress("UNCHECKED_CAST")

package com.jeovanimartinez.androidutils

import android.os.Bundle
import android.util.Log
import androidx.annotation.Size
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Clase base con propiedades y funciones comunes
 * */
abstract class Base<T : Base<T>> {

    companion object {
        /**
         * Para habilitar o deshabilitar los mensajes del log de depuración, de manera predeterminada es según BuildConfig.DEBUG,
         * esta configuración aplica para todas las clases que hereden de Base, es decir se activa o desactiva de manera global
         **/
        var logEnable = BuildConfig.DEBUG

        /**
         * Instancia de Firebase Analytics, asignar solo si se requiere que la app que implementa la biblioteca registre
         * los eventos de análisis, esta propiedad es global y se usa en todas las subclases de esta clase. Si se desea
         * desactivar el registro de eventos para una clase en específico, hacerlo mediante la propiedad firebaseAnalyticsEnabled
         * de la instancia de la clase
         * */
        var firebaseAnalyticsInstance: FirebaseAnalytics? = null
    }

    /**
     * Determina si el registro de eventos en Firebase Analytics esta habilitado para la instancia de la clase, para el registro de eventos,
     * la propiedad estática del companion object firebaseAnalyticsInstance también debe estar asignada, si esa propiedad es null, no se van
     * a registrar eventos, ya que es requerida
     * */
    var firebaseAnalyticsEnabled = true

    /** Etiqueta par el log */
    @Suppress("PropertyName")
    protected abstract val LOG_TAG: String

    /**
     * Registra un evento en Firebase Analytics, siempre y cuando la clase base tenga una instancia de Firebase Analytics,
     * y el registro de eventos este habilitado para la instancia de la clase
     *
     * @param eventName Nombre del evento, debe estar entre 1 y 40 caracteres
     * @param eventParams Parámetros para el evento (opcional)
     * */
    internal fun firebaseAnalytics(@Size(min = 1L, max = 40L) eventName: String, eventParams: Bundle? = null) {
        log("Event emitted: [ $eventName ]")

        // Si no hay instancia de Firebase Analytics,
        if (firebaseAnalyticsInstance == null) {
            log("No need to log event into Firebase Analytics, firebaseAnalyticsInstance is null")
            return
        }

        // Si el log esta deshabilitado para esta clase en específico
        if (!firebaseAnalyticsEnabled) {
            log("No need to log event into Firebase Analytics, this is disabled for this class instance")
            return
        }

        // De otro modo, se procede a registrar el evento en Firebase Analytics
        firebaseAnalyticsInstance?.logEvent(eventName, eventParams)
        log("Event logged into Firebase Analytics")
    }

    /**
     * Muestra el [message] y el [throwable] en el log de DEPURACIÓN,
     * usado para detallar el flujo de ejecución, se puede habilitar y deshabilitar con setLogEnable
     **/
    internal fun log(message: Any, throwable: Throwable? = null) {
        if (!logEnable) return
        if (throwable != null) Log.d(LOG_TAG, message.toString(), throwable)
        else Log.d(LOG_TAG, message.toString())
    }

    /** Muestra el [message] y el [throwable] en el log de ADVERTENCIA */
    internal fun logw(message: Any, throwable: Throwable? = null) {
        if (throwable != null) Log.w(LOG_TAG, message.toString(), throwable)
        else Log.w(LOG_TAG, message.toString())
    }

    /** Muestra el [message] y el [throwable] en el log de ERROR */
    internal fun loge(message: Any, throwable: Throwable? = null) {
        if (throwable != null) Log.e(LOG_TAG, message.toString(), throwable)
        else Log.e(LOG_TAG, message.toString())
    }

}
