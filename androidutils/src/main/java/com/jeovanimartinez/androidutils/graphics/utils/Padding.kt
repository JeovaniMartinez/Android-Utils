@file:Suppress("unused")

package com.jeovanimartinez.androidutils.graphics.utils

/**
 * Padding configuration.
 * @param top Top padding.
 * @param right Right padding.
 * @param bottom Bottom padding.
 * @param left Left padding.
 * */
data class Padding(val top: Float, val right: Float, val bottom: Float, val left: Float) {

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

}
