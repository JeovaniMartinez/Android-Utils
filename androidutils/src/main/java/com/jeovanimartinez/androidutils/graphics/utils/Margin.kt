@file:Suppress("unused")

package com.jeovanimartinez.androidutils.graphics.utils

import android.content.Context

/**
 * Margin configuration.
 * @param top Top margin.
 * @param right Right margin.
 * @param bottom Bottom margin.
 * @param left Left margin.
 * */
data class Margin(var top: Float, var right: Float, var bottom: Float, var left: Float) {

    /**
     * Margin configuration.
     * @param all Value to apply to all margins (top, right, bottom e left).
     * */
    constructor(all: Float) : this(all, all, all, all)

    /**
     * Margin configuration.
     * @param vertical Value for the top and bottom margin.
     * @param horizontal Value for the right and left margin.
     * */
    constructor(vertical: Float, horizontal: Float) : this(vertical, horizontal, vertical, horizontal)

    /**
     * Interprets that the current values are expressed in density-independent pixels (dp) and converts them
     * to pixels (px) according to the device screen density.
     * @param context Context from which the function is called.
     * @return This instance with the values already converted.
     * */
    fun asDpToPx(context: Context): Margin {
        val density = context.resources.displayMetrics.density
        top *= density
        right *= density
        bottom *= density
        left *= density

        return this
    }

}
