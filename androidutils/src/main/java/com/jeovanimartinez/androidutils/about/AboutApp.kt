package com.jeovanimartinez.androidutils.about

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/** Utilidad para mostrar una pantalla de acerca de la aplicación */
object AboutApp {

    /** Color de fondo de la pantalla (id del color) */
    @ColorRes
    var backgroundColor: Int? = null

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

    /**
     * Determina si se muestra un botón para ver las licencias de código abierto de la app.
     * En caso de ser afirmativo, el build.gradle del proyecto debe tener la dependencia 'com.google.android.gms:oss-licenses-plugin:VERSION'
     * y el build.gradle del proyecto debe implementar el plugin 'com.google.android.gms.oss-licenses-plugin', tal como se muestra en la documentación
     * oficial: https://developers.google.com/android/guides/opensource
     * */
    var showOpenSourceLicenses = false

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
    }

}
