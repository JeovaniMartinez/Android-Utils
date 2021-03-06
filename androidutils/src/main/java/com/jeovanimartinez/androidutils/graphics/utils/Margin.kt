@file:Suppress("unused")

package com.jeovanimartinez.androidutils.graphics.utils

/**
 * Margin configuration.
 * @param top Top margin.
 * @param right Right margin.
 * @param bottom Bottom margin.
 * @param left Left margin.
 * */
data class Margin(val top: Float, val right: Float, val bottom: Float, val left: Float) {

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

}
