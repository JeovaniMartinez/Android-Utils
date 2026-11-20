@file:Suppress("unused")

package com.jeovanimartinez.androidutils.graphics.utils

import android.content.Context

/**
 * Padding configuration.
 * @param top Top padding.
 * @param right Right padding.
 * @param bottom Bottom padding.
 * @param left Left padding.
 * */
data class Padding(var top: Float, var right: Float, var bottom: Float, var left: Float) {

    /**
     * Padding configuration.
     * @param all Value to apply to all paddings (top, right, bottom e left).
     * */
    constructor(all: Float) : this(all, all, all, all)

    /**
     * Padding configuration.
     * @param vertical Value for the top and bottom padding.
     * @param horizontal Value for the right and left padding.
     * */
    constructor(vertical: Float, horizontal: Float) : this(vertical, horizontal, vertical, horizontal)

    /**
     * Interprets that the current values are expressed in density-independent pixels (dp) and converts them
     * to pixels (px) according to the device screen density.
     * @param context Context from which the function is called.
     * @return This instance with the values already converted.
     * */
    fun asDpToPx(context: Context): Padding {
        val density = context.resources.displayMetrics.density
        top *= density
        right *= density
        bottom *= density
        left *= density

        return this
    }

}
