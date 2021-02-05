@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.basictypes

/**
 * Extensión para el tipo de dato Float
 * */

// Referencia: https://www.arduino.cc/reference/en/language/functions/math/map/
/**
 * Mapea el valor de un rango a otro y devuelve el resultado
 * @param inMin valor mínimo de la entrada
 * @param inMax valor maximo de la entrada
 * @param outMin valor mínimo de la salida
 * @param outMin valor máximo de la salida
 * Por ejemplo, inMin = 0, inMax = 1, outMin = 0, outMin = 255 - En este caso,
 * el valor de entrada debe estar entre 0 y 1, y se va a mapear a una salida
 * entre 0 y 255. Si el valor de entrada fuese 0.5 la salida es 127.5. En este
 * mismo ejemplo un valor de 2 daria como resultado 510, ya que es doble según
 * los valores establecidos.
 * */
fun Float.mapValue(inMin: Float, inMax: Float, outMin: Float, outMax: Float): Float {
    return (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
}
