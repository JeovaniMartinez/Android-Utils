@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.context

import android.content.Context
import android.widget.Toast

/**
 * Conjunto de extensiones de Toast para el el context
 * */

/** Muestra un toast de corta duración con el [message] indicado (el mensaje debe ser el id del recurso) */
fun Context.shortToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/** Muestra un toast de corta duración con el [message] indicado (el mensaje debe ser un string) */
fun Context.shortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/** Muestra un toast de larga duración con el [message] indicado (el mensaje debe ser el id del recurso) */
fun Context.longToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/** Muestra un toast de larga duración con el [message] indicado (el mensaje debe ser un string) */
fun Context.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
