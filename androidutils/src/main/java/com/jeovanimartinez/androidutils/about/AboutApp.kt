package com.jeovanimartinez.androidutils.about

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.annotation.DrawableRes

object AboutApp {

    /** Nombre de la aplicación */
    var appName: String? = null

    /** Ícono de la aplicación (id del recurso) */
    @DrawableRes
    var appIcon: Int? = null

    /** Nombre del autor */
    var authorName: String? = null

    /** URL que se abre al dar clic al nombre del autor  */
    var authorLink: String? = null

    /** Logo de la empresa (id del recurso) */
    @DrawableRes
    var companyLogo: Int? = null

    /** URL que se abre al dar clic al logo de la empresa  */
    var companyLink: String? = null

    /** URL de los términos de uso y política de privacidad */
    var termsAndPrivacyPolicyLink: String? = null

    /** Muestra la actividad de acerca de, requiere la [activity] */
    fun show(activity: Activity) {
        activity.startActivity(
            Intent(activity, AboutActivity::class.java),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ActivityOptions.makeSceneTransitionAnimation(activity).toBundle() else null
        )
    }

}
