package com.jeovanimartinez.androidutils.extensions.view

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt

/**
 * Extensions for the view groups.
 * */

/**
 * Recursively changes the text color of all TextViews in the given ViewGroup and its child views.
 * Is possible that when using this function, the color of button text and other elements may also
 * change, as they use a TextView internally.
 * @param color The color to be applied to the text.
 * */
fun ViewGroup.changeAllTextViewsTextColor(@ColorInt color: Int) {
    for (i in 0 until childCount) {
        val childView = getChildAt(i)
        if (childView is ViewGroup) {
            childView.changeAllTextViewsTextColor(color)
        } else if (childView is TextView) {
            childView.setTextColor(color)
        }
    }
}
