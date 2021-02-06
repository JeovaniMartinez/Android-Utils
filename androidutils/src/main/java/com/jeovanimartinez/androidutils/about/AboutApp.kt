package com.jeovanimartinez.androidutils.about

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R

/** Utilidad para mostrar una pantalla de acerca de la aplicación */
object AboutApp : Base<AboutApp>() {

    override val LOG_TAG = "AboutApp"

    /** Color de fondo de la actividad, si no se asigna se usa el color de fondo del tema */
    @ColorRes
    var backgroundColor = R.color.colorBackground

    /** Color para los iconos */
    @ColorRes
    var iconsColor = R.color.colorIcon

    /** Ícono o logo de la aplicación */
    @DrawableRes
    var appIcon = R.drawable.library_logo

    /** Nombre de la aplicación */
    @StringRes
    var appName = R.string.about_app_app_name

    /** Version de la aplicación */
    var appVersionName: String = ""

    /** Nombre del autor */
    @StringRes
    var authorName = R.string.about_app_author_name

    /** URL que se abre al dar clic al nombre del autor, null para no abrir ningún enlace */
    @StringRes
    var authorLink: Int? = R.string.about_app_author_link

    /** Logo de la empresa */
    @DrawableRes
    var companyLogo = R.drawable.logo_jedemm_com

    /** Nombre de la empresa */
    @StringRes
    var companyName = R.string.about_app_company_name

    /** URL que se abre al dar clic al logo de la empresa, null para no abrir ningún enlace */
    @StringRes
    var companyLink: Int? = R.string.about_app_company_link

    /** URL de los términos de uso y política de privacidad, dejar en null si la app no se desea mostrarlos */
    @StringRes
    var termsAndPrivacyPolicyLink: Int? = R.string.about_app_terms_and_policy_link

    /** Color del texto de los términos de uso y política de privacidad, si no se define, se usa el color predeterminado  */
    @ColorRes
    var termsAndPrivacyPolicyTextColor = R.color.colorTermsAndPrivacyPolicyText

    /**
     * Determina si se muestra un botón para ver las licencias de código abierto de la app.
     * En caso de ser afirmativo, el build.gradle del proyecto debe tener la dependencia 'com.google.android.gms:oss-licenses-plugin:VERSION'
     * y el build.gradle del proyecto debe implementar el plugin 'com.google.android.gms.oss-licenses-plugin', tal como se muestra en la documentación
     * oficial: https://developers.google.com/android/guides/opensource
     * */
    var showOpenSourceLicenses = true

    // Para que se pueda aplicar la task description en la actividad, taskDescriptionTitle, taskDescriptionIcon y taskDescriptionColor deben asignarse

    /**
     * Título para la Task Description de la actividad. Se usa en conjunto con taskDescriptionIcon y taskDescriptionColor y ninguna
     * propiedad debe ser null para que se configure el Task Description.
     * */
    @StringRes
    var taskDescriptionTitle: Int? = null

    /**
     * Icono para la Task Description de la actividad. Se usa en conjunto con taskDescriptionTitle y taskDescriptionColor y ninguna
     * propiedad debe ser null para que se configure el Task Description.
     * */
    @DrawableRes
    var taskDescriptionIcon: Int? = null

    /**
     * Color para la Task Description de la actividad. Se usa en conjunto con taskDescriptionTitle y taskDescriptionIcon y ninguna
     * propiedad debe ser null para que se configure el Task Description.
     * */
    @ColorRes
    var taskDescriptionColor: Int? = null

    /** Muestra la actividad de acerca de, requiere la [activity] para poder iniciar la actividad AboutActivity */
    fun show(activity: Activity) {
        activity.startActivity(
            Intent(activity, AboutActivity::class.java),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ActivityOptions.makeSceneTransitionAnimation(activity).toBundle() else null
        )
        firebaseAnalytics("about_app_shown") // Se registra el evento aquí, para evitar registrarlos más de una vez en la actividad (en caso de que sea recreada)
        log("Launched AboutActivity")
    }

}
