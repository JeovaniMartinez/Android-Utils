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

    /** Color de fondo de la actividad, si se deja en null se usa el color predeterminado del tema de la app (si esta definido) sino se usa el del tema de la biblioteca */
    @ColorRes
    var backgroundColor = R.color.colorBackground

    /** Color para los iconos */
    @ColorRes
    var iconsColor = R.color.colorIcon

    /** Ícono de la aplicación */
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

    /** URL que se abre al dar clic al nombre del autor */
    @StringRes
    var authorLink = R.string.about_app_author_link

    /** Logo de la empresa */
    @DrawableRes
    var companyLogo = R.drawable.logo_jedemm_com

    /** Nombre de la empresa */
    @StringRes
    var companyName = R.string.about_app_company_name

    /** URL que se abre al dar clic al logo de la empresa */
    @StringRes
    var companyLink = R.string.about_app_company_link

    /** URL de los términos de uso y política de privacidad */
    @StringRes
    var termsAndPrivacyPolicyLink = R.string.about_app_terms_and_policy_link

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

    /**
     * Si se desea configurar la TaskDescription para la actividad de acerca de, asignar las propiedades del objeto,
     * para que se establezca el TaskDescription, ninguna propiedad debe ser null, todas deben ser asignadas
     * */
    object TaskDescription {
        /** Título */
        @StringRes
        var title: Int? = null

        /** Ícono */
        @DrawableRes
        var icon: Int? = null

        /** Color */
        @ColorRes
        var color: Int? = null
    }

    /** Muestra la actividad de acerca de, requiere la [activity] */
    fun show(activity: Activity) {
        activity.startActivity(
            Intent(activity, AboutActivity::class.java),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ActivityOptions.makeSceneTransitionAnimation(activity).toBundle() else null
        )
        log("Launched AboutActivity")
    }

}
