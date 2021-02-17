package com.jeovanimartinez.androidutils.about

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes

/**
 * Configuración para la actividad de acerca de.
 * @param backgroundColor Color de fondo de la actividad, si es null se usa el valor predeterminado que es el color de fondo del tema.
 * @param iconsColor Color para los iconos, si es null se usa el valor predeterminado que es el color de definido en el tema.
 * @param appIcon Ícono o logo de la aplicación.
 * @param appName Nombre de la aplicación.
 * @param appVersionName Version de la aplicación.
 * @param authorName Nombre del autor.
 * @param authorLink URL que se abre al dar clic al nombre del autor, null para no abrir ningún enlace.
 * @param companyLogo Logo de la empresa.
 * @param companyName Nombre de la empresa.
 * @param companyLink URL que se abre al dar clic al logo de la empresa, null para no abrir ningún enlace.
 * @param termsAndPrivacyPolicyLink URL de los términos de uso y política de privacidad, dejar en null si no se desea mostrarlos.
 * @param termsAndPrivacyPolicyTextColor Color del texto de los términos de uso y política de privacidad, si es null se usa el valor predeterminado que es el color definido en el tema.
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
data class AboutAppConfig(
    @ColorInt val backgroundColor: Int? = null,
    @ColorInt val iconsColor: Int? = null,
    @DrawableOrDrawableRes val appIcon: Any?,
    @StringOrStringRes val appName: Any,
    @StringOrStringRes val appVersionName: Any,
    @StringOrStringRes val authorName: Any,
    @StringOrStringRes val authorLink: Any?,
    @DrawableOrDrawableRes val companyLogo: Any?,
    @StringOrStringRes val companyName: Any,
    @StringOrStringRes val companyLink: Any?,
    @StringOrStringRes val termsAndPrivacyPolicyLink: Any?,
    @ColorInt val termsAndPrivacyPolicyTextColor: Int? = null,
    val showOpenSourceLicenses: Boolean,
    @StringOrStringRes val taskDescriptionTitle: Any? = null,
    @DrawableRes val taskDescriptionIcon: Int? = null,
    @ColorInt val taskDescriptionColor: Int? = null
)
