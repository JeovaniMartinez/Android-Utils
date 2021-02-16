@file:Suppress("CanBePrimaryConstructorProperty")

package com.jeovanimartinez.androidutils.about

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat

/**
 * Utilidad para mostrar una actividad de acerca de la aplicación.
 * @param activity Actividad para poder iniciar la AboutActivity.
 * @param backgroundColor Color de fondo de la actividad, el valor predeterminado es el color de fondo del tema.
 * @param iconsColor Color para los iconos, el valor predeterminado es el color de definido en el tema.
 * @param appIcon Ícono o logo de la aplicación.
 * @param appName Nombre de la aplicación.
 * @param appVersionName Version de la aplicación.
 * @param authorName Nombre del autor.
 * @param authorLink URL que se abre al dar clic al nombre del autor, null para no abrir ningún enlace.
 * @param companyLogo Logo de la empresa.
 * @param companyName Nombre de la empresa.
 * @param companyLink URL que se abre al dar clic al logo de la empresa, null para no abrir ningún enlace.
 * @param termsAndPrivacyPolicyLink URL de los términos de uso y política de privacidad, dejar en null si no se desea mostrarlos.
 * @param termsAndPrivacyPolicyTextColor Color del texto de los términos de uso y política de privacidad, el valor predeterminado es el color de definido en el tema.
 * @param showOpenSourceLicenses Determina si se muestra un botón para ver las licencias de código abierto de la app. En caso de ser afirmativo, el build.gradle
 *        del proyecto debe tener la dependencia 'com.google.android.gms:oss-licenses-plugin:VERSION' y el build.gradle del proyecto debe implementar el plugin
 *        'com.google.android.gms.oss-licenses-plugin', tal como se muestra en la documentación oficial: https://developers.google.com/android/guides/opensource
 * @param taskDescriptionTitle Título para la Task Description de la actividad. Se usa en conjunto con taskDescriptionIcon y taskDescriptionColor, ninguno de estos tres parámetros
 *        debe ser null para que se configure el Task Description.
 * @param taskDescriptionIcon Ícono para la Task Description de la actividad. Se usa en conjunto con taskDescriptionTitle y taskDescriptionColor, ninguno de estos tres parámetros
 *        debe ser null para que se configure el Task Description.
 * @param taskDescriptionColor Color para la Task Description de la actividad. Se usa en conjunto con taskDescriptionTitle y taskDescriptionIcon, ninguno de estos tres parámetros
 *        debe ser null para que se configure el Task Description.
 * */
class AboutApp(
    private val activity: Activity,
    @ColorInt val backgroundColor: Int = activity.getColorCompat(R.color.colorBackground),
    @ColorInt val iconsColor: Int = activity.getColorCompat(R.color.colorIcon),
    @DrawableOrDrawableRes val appIcon: Any?,
    @StringOrStringRes val appName: Any,
    @StringOrStringRes val appVersionName: Any,
    @StringOrStringRes val authorName: Any,
    @StringOrStringRes val authorLink: Any?,
    @DrawableOrDrawableRes val companyLogo: Any?,
    @StringOrStringRes val companyName: Any,
    @StringOrStringRes val companyLink: Any?,
    @StringOrStringRes val termsAndPrivacyPolicyLink: Any?,
    @ColorInt val termsAndPrivacyPolicyTextColor: Int = activity.getColorCompat(R.color.colorTermsAndPrivacyPolicyText),
    val showOpenSourceLicenses: Boolean,
    @StringOrStringRes val taskDescriptionTitle: Any?,
    @DrawableRes val taskDescriptionIcon: Int?,
    @ColorInt val taskDescriptionColor: Int?
) : Base<AboutApp>() {

    override val LOG_TAG = "AboutApp"

    companion object {
        lateinit var instance: AboutApp // Para poder usar en AboutActivity las propiedades y métodos de instancia de la clase
    }

    /** Muestra la actividad de acerca de */
    fun show() {
        instance = this
        activity.startActivity(
            Intent(activity, AboutActivity::class.java),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ActivityOptions.makeSceneTransitionAnimation(activity).toBundle() else null
        )
        firebaseAnalytics("about_app_shown") // Se registra el evento aquí, para evitar registrarlos más de una vez en la actividad (en caso de que sea recreada)
        log("Launched AboutActivity")
    }

}
