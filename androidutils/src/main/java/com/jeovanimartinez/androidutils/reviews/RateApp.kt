package com.jeovanimartinez.androidutils.reviews

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.jeovanimartinez.androidutils.BuildConfig
import com.jeovanimartinez.androidutils.R

/**
 * Utilidad para dirigir al usuario a Google Play, específicamente a los detalles de la aplicación
 * El uso principal es para invitar al usuario a que califique la aplicación, se abren los detalles generales de la app,
 * ya que no hay manera de mandarlo directamente a calificar la app con este método
 * */
object RateApp {

    private const val LOG_TAG = "RateApp"

    // Para habilitar o deshabilitar los mensajes del log
    var logEnable = BuildConfig.DEBUG

    /**
     * Dirige al usuario a los detalles de la aplicación en Google Play para que pueda calificar la aplicación
     * Si es posible, se abre la aplicación directamente en la app de Google Play, en caso de no ser posible, se abre
     * en el navegador, si tampoco es posible, muestra un toast con un mensaje
     * Referencia: https://stackoverflow.com/questions/10816757/rate-this-app-link-in-google-play-store-app-on-the-phone
     *
     * @param activity actividad desde donde se llama, se usa para iniciar otras actividades
     * @param firebaseAnalytics instancia de FirebaseAnalytics por si se desean registrar los eventos, dejar en null si no se requiere
     * */
    fun rateInGooglePlay(activity: Activity, firebaseAnalytics: FirebaseAnalytics? = null) {
        val marketUriString = "market://details?id=${activity.packageName}"
        val uri = Uri.parse(marketUriString)
        val googlePlayIntent = Intent(Intent.ACTION_VIEW, uri)
        googlePlayIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            activity.startActivity(googlePlayIntent) // Se intenta mostrar en la app de google play
            if (logEnable) Log.i(LOG_TAG, "Sent user to view app details in google play app [$marketUriString]")
            firebaseAnalytics?.logEvent("rate_app_sent_to_google_play_app", null)
        } catch (e1: ActivityNotFoundException) {
            try {
                // Si no se puede mostrar en la app de google play, se intenta abrir en el navegador web
                val webUriString = "http://play.google.com/store/apps/details?id=${activity.packageName}"
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webUriString)))
                if (logEnable) Log.i(LOG_TAG, "Sent user to view app details in google play on web browser [$webUriString]")
                firebaseAnalytics?.logEvent("rate_app_sent_to_google_play_web", null)
            } catch (e2: ActivityNotFoundException) {
                // Si no se pudo mostrar en ninguna de las dos maneras anteriores, muestra un mensaje
                Toast.makeText(activity, R.string.rate_app_unable_to_show_app_on_google_play, Toast.LENGTH_SHORT).show()
                if (logEnable) Log.i(LOG_TAG, "Unable to sent user to app details, google play app and web browser are not available")
                firebaseAnalytics?.logEvent("rate_app_unable_to_show_on_google_play", null)
            }
        }
    }

}
