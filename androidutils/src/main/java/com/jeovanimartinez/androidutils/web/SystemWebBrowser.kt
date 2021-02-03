package com.jeovanimartinez.androidutils.web

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.URLUtil
import androidx.annotation.Size
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.extensions.context.shortToast
import java.lang.Exception

/**
 * Utilidad para interactuar con el navegador web del sistema
 * */
object SystemWebBrowser : Base<SystemWebBrowser>() {

    override val LOG_TAG = "SystemWebBrowser"

    /**
     * Abre el navegador web del sistema en la [url] especificada
     * @param context contexto para poder lanzar el intent del navegador
     * @param url URL a mostrar, debe ser una URL completa, que incluya http o https, de otro modo no va a pasar la validación
     * @param case razón por la que se abre la URL en el navegador. Esto aplica solo si Firebase Analytics esta habilitado, cuando se muestra
     *        una URL en el navegador se registra el evento, este evento contiene un parámetro que ayuda a determinar que sitio web se mostró,
     *        y se registra el valor de case. Debe tener entre 1 y 100 caracteres.
     * */
    fun openUrl(context: Context, url: String, @Size(min = 1L, max = 100L) case: String? = null) {
        // Se valida la URL
        if (!URLUtil.isValidUrl(url)) return logw("The URL [$url] is not a valid URL")

        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) // Se inicia la actividad
            if (case != null && case.isNotBlank()) {
                // Se registra el evento y el caso como parámetro
                firebaseAnalytics("open_url_in_system_web_browser", Bundle().apply { putString("case", case.trim()) })
            } else {
                // Solo se registra el evento
                firebaseAnalytics("open_url_in_system_web_browser")
            }
            log("URL: [$url] opened, case: $case")
        } catch (e: ActivityNotFoundException) {
            // No Hay ninguna aplicación que pueda abrir la URL
            context.shortToast(R.string.system_web_browser_not_available)
            firebaseAnalytics("open_url_in_system_web_browser", Bundle().apply { putString("case", "activity_not_found_exception") })
            logw("Unable to open URL [$url], web browser not available", e)
        } catch (e: Exception) {
            // Excepción general
            context.shortToast(R.string.system_web_browser_error)
            firebaseAnalytics("open_url_in_system_web_browser", Bundle().apply { putString("case", "exception") })
            loge("Error opening URL [$url]", e)
        }
    }

}
