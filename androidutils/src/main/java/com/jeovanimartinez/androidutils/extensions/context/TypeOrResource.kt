package com.jeovanimartinez.androidutils.extensions.context

import android.content.Context
import java.lang.Exception

/**
 * Conjunto de extensiones para tratar un valor como un tipo de dato, o bien
 * obtener el valor de los recursos
 * */

/**
 * Analiza el tipo de dato recibido [value] y devuelve siempre un string.
 * Si el tipo de dato es string lo devuelve tal cual, sin procesar.
 * Si el tipo de dato es char, lo devuelve como string.
 * Si el tipo de dato es entero, se asume que es un ID de recurso, por lo que se obtiene y devuelve el texto, en caso de no existir el recurso, lanza una excepción.
 * Para cualquier otro tipo de dato lanza una excepción, ya que el dato no se puede tratar directamente como string.
 * */
fun Context.typeAsString(value: Any): String {
    return when (value) {
        is String -> return value // Si es string, se devuelve tal cual
        is Char -> return value.toString() // Si es char se devuelve como string
        is Int -> this.getString(value) // Si es un entero, se asume que es el ID de un recurso por lo que se obtiene, si el recurso no existe, se genera una excepción
        else -> throw Exception("Expected value type string object or int (for get the string resource by ID) but received value type ${value.javaClass}") // Tipo de dato no válido
    }
}
