@file:Suppress("UNCHECKED_CAST")

package com.jeovanimartinez.androidutils

import android.util.Log
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
    }

    /** Etiqueta par el log */
    @Suppress("PropertyName")
    protected abstract val LOG_TAG: String

    /** Instancia de Firebase Analytics para registro de eventos, asignar solo si se requiere */
    protected var firebaseAnalytics: FirebaseAnalytics? = null

    /** Asigna la instancia de [firebaseAnalytics] para registro de eventos */
    fun setFirebaseAnalyticsInstance(firebaseAnalytics: FirebaseAnalytics): T {
        this.firebaseAnalytics = firebaseAnalytics
        return this as T
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
