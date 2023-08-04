package com.jeovanimartinez.androidutils.extensions.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.ViewPropertyAnimator

/**
 * Extensions for animations in views.
 * */

// Reference: https://antonioleiva.com/listeners-several-functions-kotlin/
/**
 * Callback for when the animation ends.
 *
 * Example:
 *```
 *     // Show a toast on animation end.
 *     demo.animate().translationX(0f).onAnimationEnd {  shortToast("Hello") }
 *```
 * */
inline fun ViewPropertyAnimator.onAnimationEnd(crossinline continuation: (Animator) -> Unit) {
    setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            continuation(animation)

            // Remove the callback to prevent it from being called again in another view animation
            setListener(null)
        }
    })
}
