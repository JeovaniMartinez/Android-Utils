@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.dimension

import android.content.Context
import android.view.View
import androidx.annotation.DimenRes

/**
 * Extensions available for any view and any context, allow you to easily convert dp or sp to px and vice versa.
 * The content is identical to Dimensions.kt, only that here the values are returned as Float
 * */

/** Converts dp to px. Return the result as Float */
fun Context.dp2pxF(value: Int): Float = value * resources.displayMetrics.density

/** Converts dp to px. Return the result as Float */
fun View.dp2pxF(value: Int): Float = context.dp2pxF(value)

/** Converts dp to px. Return the result as Float */
fun Context.dp2pxF(value: Float): Float = value * resources.displayMetrics.density

/** Converts dp to px. Return the result as Float */
fun View.dp2pxF(value: Float): Float = context.dp2pxF(value)

/** Converts sp to px. Return the result as Float */
fun Context.sp2pxF(value: Int): Float = value * resources.displayMetrics.scaledDensity

/** Converts sp to px. Return the result as Float */
fun View.sp2pxF(value: Int): Float = context.sp2pxF(value)

/** Converts sp to px. Return the result as Float */
fun Context.sp2pxF(value: Float): Float = value * resources.displayMetrics.scaledDensity

/** Converts sp to px. Return the result as Float */
fun View.sp2pxF(value: Float): Float = context.sp2pxF(value)

/** Converts px to dp. Return the result as Float */
fun Context.px2dpF(value: Int): Float = value / resources.displayMetrics.density

/** Converts px to dp. Return the result as Float */
fun View.px2dpF(value: Int): Float = context.px2dpF(value)

/** Converts px to dp. Return the result as Float */
fun Context.px2dpF(value: Float): Float = value / resources.displayMetrics.density

/** Converts px to dp. Return the result as Float */
fun View.px2dpF(value: Float): Float = context.px2dpF(value)

/** Converts px to sp. Return the result as Float */
fun Context.px2spF(value: Int): Float = value / resources.displayMetrics.scaledDensity

/** Converts px to sp. Return the result as Float */
fun View.px2spF(value: Int): Float = context.px2spF(value)

/** Converts px to sp. Return the result as Float */
fun Context.px2spF(value: Float): Float = value / resources.displayMetrics.scaledDensity

/** Converts px to sp. Return the result as Float */
fun View.px2spF(value: Float): Float = context.px2spF(value)

/** Retrieve a dimensional for a particular resource ID for use as a size in raw pixels. Return the result as Float */
fun Context.dimenF(@DimenRes resource: Int): Float = resources.getDimensionPixelSize(resource).toFloat()

/** Retrieve a dimensional for a particular resource ID for use as a size in raw pixels. Return the result as Float */
fun View.dimenF(@DimenRes resource: Int): Float = context.dimen(resource).toFloat()
