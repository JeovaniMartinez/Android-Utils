@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.graphics

import android.content.Context
import com.jeovanimartinez.androidutils.graphics.utils.Margin
import com.jeovanimartinez.androidutils.graphics.utils.Padding

/**
 * Set of extensions for the Margin and Padding classes.
 * */

/**
 * Converts all density-independent pixels (dp) margin values to pixels (px) and returns them to a new object.
 * > For example, if the value of margin top = 10, it will be assumed that this value is represented in dp, so
 * the conversion to pixels is carried out according to the density of the screen, and the result of that
 * conversion will be the margin top of the returned object.
 * @param context Context
 * @return A new Margin object with the converted values.
 * */
fun Margin.dp2px(context: Context): Margin {
    val top = this.top * context.resources.displayMetrics.density
    val right = this.right * context.resources.displayMetrics.density
    val bottom = this.bottom * context.resources.displayMetrics.density
    val left = this.left * context.resources.displayMetrics.density
    return Margin(top, right, bottom, left)
}

/**
 * Converts all density-independent pixels (dp) padding values to pixels (px) and returns them to a new object.
 * > For example, if the value of padding top = 10, it will be assumed that this value is represented in dp, so
 * the conversion to pixels is carried out according to the density of the screen, and the result of that
 * conversion will be the padding top of the returned object.
 * @param context Context
 * @return A new Padding object with the converted values.
 * */
fun Padding.dp2px(context: Context): Padding {
    val top = this.top * context.resources.displayMetrics.density
    val right = this.right * context.resources.displayMetrics.density
    val bottom = context.resources.displayMetrics.density
    val left = context.resources.displayMetrics.density
    return Padding(top, right, bottom, left)
}
