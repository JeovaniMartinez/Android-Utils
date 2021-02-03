package com.jeovanimartinez.androidutils.reviews

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.extensions.context.shortToast

/**
 * Utilidad para dirigir al usuario a Google Play, específicamente a los detalles de la aplicación.
 * El uso principal es para invitar al usuario a que califique la aplicación, se abren los detalles generales de la app,
 * ya que no hay manera de mandarlo directamente a calificar la app con este método.
 * */
object RateApp : Base<RateApp>() {

    override val LOG_TAG = "RateApp"

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
