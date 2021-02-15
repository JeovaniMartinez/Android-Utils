@file:Suppress("CanBePrimaryConstructorProperty")

package com.jeovanimartinez.androidutils.about

import android.content.Context
import androidx.annotation.ColorInt
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat

/**
 * Utilidad para mostrar una actividad de acerca de la aplicación.
 * @param context Contexto.
 * @param backgroundColor Color de fondo de la actividad, el valor predeterminado es el color de fondo del tema.
 * @param iconsColor Color para los iconos.
 * @param appName Nombre de la aplicación.
 * */
class AboutConfig(
    context: Context,
    @ColorInt backgroundColor: Int = context.getColorCompat(R.color.colorBackground),
    @ColorInt iconsColor: Int = context.getColorCompat(R.color.colorIcon),
    @StringOrStringRes appName: Any
) {
    /** Color de fondo de la actividad, el valor predeterminado es el color de fondo del tema. */
    val backgroundColor = backgroundColor
    /** Color para los iconos. */
    val iconsColor = iconsColor
    val appName = appName

    fun show(){

    }
}
