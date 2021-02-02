@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.activity

import android.app.Activity
import android.app.ActivityManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat

/**
 * Extensión para para configurar el TaskDescription de las actividades
 * */

/**
 * Configura la TaskDescription de la actividad, funciona desde Android 5 en adelante y ajusta la configuración
 * automáticamente para las diferentes versiones de Android
 * @param title título para la descripción (id del recurso). Debe ser una imagen PNG, si el id del recurso es un SVG, no se va a aplicar
 *              y se va a mostrar el ícono de la app.
 * @param icon ícono a mostrar (id del recurso)
 * @param color color de fondo de la barra (id del recurso)
 * */
fun Activity.configureTaskDescription(@StringRes title: Int, @DrawableRes icon: Int, @ColorRes color: Int) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

        // Para Android Pie (9.0) API 28 y versiones posteriores

        val taskDescription = ActivityManager.TaskDescription(getString(title), icon, ResourcesCompat.getColor(resources, color, this.theme))
        this.setTaskDescription(taskDescription)

    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        // De Android 5.0 a Android 8.1

        @Suppress("DEPRECATION")
        val taskDescription = ActivityManager.TaskDescription(
            getString(title),
            BitmapFactory.decodeResource(resources, icon),
            ResourcesCompat.getColor(resources, color, this.theme)
        )
        this.setTaskDescription(taskDescription)

    }

    // No hay soporte para versiones anteriores a Android 5

}
