package com.jeovanimartinez.androidutils.moreapps

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
 * Utilidad para dirigir al usuario a Google Play, específicamente a la lista de aplicaciones del desarrollador
 * El uso principal es para invitar al usuario a que instale otras aplicaciones del desarrollador
 * */
object MoreAppsGPlay {

    private const val LOG_TAG = "MoreAppsGPlay"

    // ID del desarrollador
    var developerId = "Jedemm+Technologies"

    // Para habilitar o deshabilitar los mensajes del log
    var logEnable = BuildConfig.DEBUG

    /**
     * Dirige al usuario a la lista de aplicaciones del desarrollador, en base a su ID (developerId)
     * Si no es posible, muestra un toast con un mensaje
     *
     * @param activity actividad desde donde se llama, se usa para iniciar otras actividades
     * @param firebaseAnalytics instancia de FirebaseAnalytics por si se desean registrar los eventos, dejar en null si no se requiere
     * */
    fun showAppList(activity: Activity, firebaseAnalytics: FirebaseAnalytics? = null) {
        try {
            // Se intenta abrir en el navegador web la página del desarrollador (si el usuario tiene Google Play se muestra ahí)
            val webUriString = "https://play.google.com/store/apps/developer?id=$developerId"
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webUriString)))
            if (logEnable) Log.i(LOG_TAG, "Sent user to view developer page in google play [$webUriString]")
            firebaseAnalytics?.logEvent("more_apps_sent_to_google_play", null)
        } catch (ex: ActivityNotFoundException) {
            // Si no se pudo mostrar, se muestra un mensaje
            Toast.makeText(activity, R.string.more_apps_unable_to_show_dev_page, Toast.LENGTH_SHORT).show()
            if (logEnable) Log.i(LOG_TAG, "Unable to send user to developer page")
            firebaseAnalytics?.logEvent("more_apps_unable_to_show_dev_page", null)
        }
    }

}
