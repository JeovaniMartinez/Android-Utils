@file:Suppress("unused")

package com.jeovanimartinez.androidutils.graphics.utils

import android.content.Context

/**
 * Corner radius configuration.
 * @param topLeftX Top left radius for x-axis.
 * @param topLeftY Top left radius for y-axis.
 * @param topRightX Top right radius for x-axis.
 * @param topRightY Top right radius for y-axis.
 * @param bottomRightX Bottom right radius for x-axis.
 * @param bottomRightY Bottom right radius for y-axis.
 * @param bottomLeftX Bottom left radius for x-axis.
 * @param bottomLeftY Bottom left radius for y-axis.
 * */
data class CornerRadius(
    var topLeftX: Float, var topLeftY: Float,
    var topRightX: Float, var topRightY: Float,
    var bottomRightX: Float, var bottomRightY: Float,
    var bottomLeftX: Float, var bottomLeftY: Float,
) {

    /**
     * Corner radius configuration.
     * @param radius Radius for all corners.
     * */
    constructor(radius: Float) : this(radius, radius, radius, radius, radius, radius, radius, radius)

    /**
     * Corner radius configuration.
     * @param radius Radius for all corners, in the pair values, first is for the x-axis and second is for the y-axis.
     * */
    constructor(radius: Pair<Float, Float>)
            : this(radius.first, radius.second, radius.first, radius.second, radius.first, radius.second, radius.first, radius.second)

    /**
     * Corner radius configuration.
     * @param topLeft Top left radius.
     * @param topRight Top right radius.
     * @param bottomRight Bottom right radius.
     * @param bottomLeft Bottom left radius.
     * */
    constructor(topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float)
            : this(topLeft, topLeft, topRight, topRight, bottomRight, bottomRight, bottomLeft, bottomLeft)

    /**
     * Converts the corner radius values to radii (array of 8 values, 4 pairs of (X,Y))
     * @return Corner radius values as radii in FloatArray.
     * */
    fun toRadii(): FloatArray {
        return floatArrayOf(
            topLeftX, topLeftY,         // Top left radius
            topRightX, topRightY,       // Top right radius
            bottomRightX, bottomRightY, // Bottom right radius
            bottomLeftX, bottomRightY   // Bottom left radius
        )
    }

    /**
     * Interprets that the current values are expressed in density-independent pixels (dp) and converts them
     * to pixels (px) according to the device screen density.
     * @param context Context.
     * @return This instance with the values already converted.
     * */
    fun asDpToPx(context: Context): CornerRadius {
        val density = context.resources.displayMetrics.density
        topLeftX *= density
        topLeftY *= density
        topRightX *= density
        topRightY *= density
        bottomRightX *= density
        bottomRightY *= density
        bottomLeftX *= density
        bottomLeftY *= density

        return this
    }

}
