package com.jeovanimartinez.androidutils.moreapps

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.extensions.context.shortToast

/**
 * Utilidad para dirigir al usuario a Google Play, específicamente a la lista de aplicaciones del desarrollador.
 * El uso principal es para invitar al usuario a que instale otras aplicaciones del desarrollador.
 * */
object MoreAppsGPlay : Base<MoreAppsGPlay>() {

    override val LOG_TAG = "MoreAppsGPlay"

    /** ID del desarrollador del cual se va a mostrar la lista de aplicaciones al llamar a showAppList */
    var developerId = "Jedemm+Technologies"

    /**
     * Dirige al usuario a la lista de aplicaciones del desarrollador, en base a su ID (developerId).
     * Si no es posible, muestra un toast con un mensaje.
     *
     * @param activity actividad desde donde se llama, se usa para iniciar otras actividades
     * */
    fun showAppList(activity: Activity) {
        try {
            // Se intenta abrir en el navegador web la página del desarrollador (si el usuario tiene Google Play se muestra ahí)
            val webUriString = "https://play.google.com/store/apps/developer?id=$developerId"
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webUriString)))
            log("Sent user to view developer page in google play [$webUriString]")
            firebaseAnalytics?.logEvent("more_apps_sent_to_google_play", null)
        } catch (ex: ActivityNotFoundException) {
            // Si no se pudo mostrar, se muestra un mensaje
            activity.shortToast(R.string.more_apps_unable_to_show_dev_page)
            log("Unable to send user to developer page", ex)
            firebaseAnalytics?.logEvent("more_apps_unable_to_show_dev_page", null)
        }
    }

}
