@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.activity

import android.app.Activity
import android.app.ActivityManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes
import com.jeovanimartinez.androidutils.extensions.context.typeAsString

/**
 * Extensión para para configurar el TaskDescription de las actividades
 * */

/**
 * Configura la TaskDescription de la actividad, funciona desde Android 5 en adelante y ajusta la configuración
 * automáticamente para las diferentes versiones de Android.
 * @param title título para la descripción, string o id del recurso.
 * @param icon ícono a mostrar (id del recurso), debe ser una imagen PNG, si el id del recurso es un SVG, no se va a aplicar
 *        y se va a mostrar el ícono de la app.
 * @param color color-int de fondo para la barra.
 * */
fun Activity.configureTaskDescription(@StringOrStringRes title: Any, @DrawableRes icon: Int, @ColorInt color: Int) {

    val finalTitle = typeAsString(title)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

        // Para Android Pie (9.0) API 28 y versiones posteriores
        val taskDescription = ActivityManager.TaskDescription(finalTitle, icon, color)
        this.setTaskDescription(taskDescription)

    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        // De Android 5.0 a Android 8.1
        @Suppress("DEPRECATION")
        val taskDescription = ActivityManager.TaskDescription(finalTitle, BitmapFactory.decodeResource(resources, icon), color)
        this.setTaskDescription(taskDescription)

    }

    // No hay soporte para versiones anteriores a Android 5

}
