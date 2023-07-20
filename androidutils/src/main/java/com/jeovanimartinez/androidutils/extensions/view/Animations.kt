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
 * It is very important to always call this function when the animation of a view is modified,
 * even if nothing is required to be done when the animation ends, this is in order to replace the
 * existing callback and prevent the code of the previous callback from being executed. For example:
 *```
 *     // Show a toast on animation end.
 *     demo.animate().translationX(0f).onAnimationEnd {  shortToast("Hello") }
 *
 *     // Don't do it this way, if you don't call .onAnimationEnd { }
 *     // The previous callback will be executed and the toast will be shown
 *     demo.animate().translationX(10f)
 *
 *     // The correct way to animate again.
 *     demo.animate().translationX(10f).onAnimationEnd { }
 *```
 * */
inline fun ViewPropertyAnimator.onAnimationEnd(crossinline continuation: (Animator) -> Unit) {
    setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            continuation(animation)
        }
    })
}
