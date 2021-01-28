@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.ViewPropertyAnimator

/**
 * Conjunto de extensiones para las animaciones de vistas
 * */

// Referencia: https://antonioleiva.com/listeners-several-functions-kotlin/
/**
 * Callback para cuando finaliza la animación.
 * Es importante llamar siempre a esta función cuando se modifique la animación de una vista, incluso si no se requiere hacer
 * nada cuando finalize la animación, esto con el fin de reemplazar el callback existente y evitar que se ejecute el código
 * de otro callback de una animación ejecutada anteriormente en la vista.
 * */
inline fun ViewPropertyAnimator.onAnimationEnd(crossinline continuation: (Animator) -> Unit) {
    setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            continuation(animation)
        }
    })
}
