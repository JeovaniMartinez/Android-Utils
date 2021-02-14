@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.context

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes
import java.lang.Exception

/**
 * Conjunto de extensiones para tratar un valor como un tipo de dato, o bien
 * obtener el valor de los recursos.
 * */

/**
 * Analiza el tipo de dato recibido [stringOrStringRes] y devuelve siempre un string.
 * - Si el tipo de dato es string lo devuelve tal cual, sin procesar.
 * - Si el tipo de dato es char, lo devuelve como string.
 * - Si el tipo de dato es entero, se asume que es un ID de recurso, por lo que se obtiene y devuelve el texto, en caso de no existir el recurso, lanza una excepción.
 * - Para cualquier otro tipo de dato lanza una excepción, ya que el dato no se puede tratar directamente como string.
 * */
fun Context.typeAsString(@StringOrStringRes stringOrStringRes: Any): String {
    return when (stringOrStringRes) {
        is String -> stringOrStringRes // Si es string, se devuelve tal cual
        is Char -> stringOrStringRes.toString() // Si es char se devuelve como string
        is Int -> this.getString(stringOrStringRes) // Si es un entero, se asume que es el ID de un recurso por lo que se obtiene, si el recurso no existe, se genera una excepción
        else -> throw Exception("Expected value type string object or int (for get the string resource by ID) but received value type ${stringOrStringRes.javaClass}") // Tipo de dato no válido
    }
}

/**
 * Analiza el tipo de dato recibido [drawableOrDrawableRes] y devuelve siempre un Drawable.
 * - Si el tipo de dato es Drawable lo devuelve tal cual, sin procesar.
 * - Si el tipo de dato es entero, se asume que es un ID de recurso, por lo que se obtiene y devuelve el Drawable, en caso de no existir el recurso, lanza una excepción.
 * - Para cualquier otro tipo de dato lanza una excepción, ya que el dato no se puede tratar directamente como Drawable.
 *
 * El valor de retorno puede ser null de acuerdo a la definición de ContextCompat.getDrawable()
 * */
fun Context.typeAsDrawable(@DrawableOrDrawableRes drawableOrDrawableRes: Any): Drawable? {
    return when (drawableOrDrawableRes) {
        is Drawable -> drawableOrDrawableRes // Si es drawable, se devuelve tal cual
        is Int -> ContextCompat.getDrawable(this, drawableOrDrawableRes) // Si es un entero, se asume que es el ID de un recurso por lo que se obtiene, si el recurso no existe, se genera una excepción
        else -> throw Exception("Expected value type drawable object or int (for get the drawable resource by ID) but received value type ${drawableOrDrawableRes.javaClass}") // Tipo de dato no válido
    }
}
