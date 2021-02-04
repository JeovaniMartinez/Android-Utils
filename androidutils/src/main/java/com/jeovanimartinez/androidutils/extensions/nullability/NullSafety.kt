@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.nullability

/**
 * Conjunto de extensiones para trabajar con la seguridad contra nulos
 * */

// Referencia: https://discuss.kotlinlang.org/t/let-vs-if-not-null/3542/2
/**
 * Ejecuta el código del bloque solo si la variable no es null. Dentro de la función, siempre usar it para hacer referencia al valor de la variable. Por ejemplo:
 *  demo.whenNotNull {
 *      // Usar siempre it en lugar del nombre de la variable, ya que la variable puede ser null, it nunca lo va a ser si entra al bloque
 *      Log.d("Check Nullability", it)
 *  }
 * Funciona de igual manera si se llama con el operador de llamada segura (?) o sin el (demo.whenNotNull es equivalente a demo?.whenNotNull).
 * Para efectos prácticos, se recomienda llamar sin el aperador de llamada segura.
 * */
fun <T : Any> T?.whenNotNull(f: (it: T) -> Unit) {
    if (this != null) f(this)
}

/**
 * Verifica si la variable es null. Devuelve true si la variable es null, y false si la variable no es null.
 * Invocar siempre sin el operador de llamada segura (?).
 * Ejemplos:
 *          val a: String? = null // a.isNull() = true;
 *          val b: String? = "demo" // b.isNull() = false;
 * */
fun Any?.isNull(): Boolean {
    return this == null
}

/**
 * Verifica si la variable es null. Si la variable es null devuelve false, si no es null devuelve true.
 * Invocar siempre sin el operador de llamada segura (?).
 * Ejemplos:
 *          val a: String? = null // a.isNotNull() = false;
 *          val b: String? = "demo" // b.isNotNull() = true;
 * */
fun Any?.isNotNull(): Boolean {
    return this != null
}
