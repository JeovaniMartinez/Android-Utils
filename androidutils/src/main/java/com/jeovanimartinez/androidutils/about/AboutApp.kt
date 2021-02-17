package com.jeovanimartinez.androidutils.about

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.extensions.nullability.isNull

/**
 * Utilidad para mostrar una actividad de acerca de la aplicación.
 * */
object AboutApp : Base<AboutApp>() {

    override val LOG_TAG = "AboutApp"

    /**
     * Se asigna antes de iniciar AboutActivity para poder usarlo en esa actividad, se usa de esta manera ya que
     * AboutAppConfig contiene objetos no serializables, y poner el objeto en los extra del intent no es posible.
     * */
    internal var currentConfig: AboutAppConfig? = null

    /**
     * Muestra la actividad de acerca de.
     * @param activity Actividad.
     * @param aboutAppConfig Objeto de configuración para la actividad de acerca de.
     * */
    fun show(activity: Activity, aboutAppConfig: AboutAppConfig) {

        // Si la actividad ya esta en ejecución
        if (AboutActivity.aboutActivityRunning) {
            log("AboutActivity is running and only one instance of this activity is allowed")
            return
        }

        // Se obtiene el valor de los colores de la configuración
        var backgroundColor = aboutAppConfig.backgroundColor
        var iconsColor = aboutAppConfig.iconsColor
        var termsAndPrivacyPolicyTextColor = aboutAppConfig.termsAndPrivacyPolicyTextColor
        // aboutAppConfig.taskDescriptionColor no es necesario, ya que su valor no es obligatorio, y puede ser null

        // Para los colores en null, se obtiene su color predeterminado
        if (backgroundColor.isNull()) backgroundColor = activity.getColorCompat(R.color.colorBackground)
        if (iconsColor.isNull()) iconsColor = activity.getColorCompat(R.color.colorIcon)
        if (termsAndPrivacyPolicyTextColor.isNull()) termsAndPrivacyPolicyTextColor = activity.getColorCompat(R.color.colorTermsAndPrivacyPolicyText)

        // Se genera el objeto final de configuración y se asigna al singleton para poder usar los datos en la AboutActivity
        currentConfig = aboutAppConfig.copy(backgroundColor = backgroundColor, iconsColor = iconsColor, termsAndPrivacyPolicyTextColor = termsAndPrivacyPolicyTextColor)

        AboutActivity.aboutActivityRunning = true // Se hace true ya que se va a iniciar la actividad

        // Se inicia la actividad de acerca de, currentConfig ya esta listo para usarse en dicha actividad
        activity.startActivity(
            Intent(activity, AboutActivity::class.java),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ActivityOptions.makeSceneTransitionAnimation(activity).toBundle() else null
        )

        firebaseAnalytics("about_app_shown") // Se registra el evento aquí, para evitar registrarlos más de una vez en la actividad (en caso de que sea recreada)
        log("Launched AboutActivity")
    }

}
