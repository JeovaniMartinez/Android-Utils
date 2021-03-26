@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.dimension

import android.content.Context
import android.view.View
import androidx.annotation.DimenRes

/**
 * Extensions available for any view and any context, allow you to easily convert dp or sp to px and vice versa.
 * Reference: https://github.com/Kotlin/anko/blob/master/anko/library/static/commons/src/main/java/Dimensions.kt
 * */

/** Converts dp to px. Return the result as Int */
fun Context.dp2px(value: Int): Int = (value * resources.displayMetrics.density).toInt()

/** Converts dp to px. Return the result as Int */
fun View.dp2px(value: Int): Int = context.dp2px(value)

/** Converts dp to px. Return the result as Int */
fun Context.dp2px(value: Float): Int = (value * resources.displayMetrics.density).toInt()

/** Converts dp to px. Return the result as Int */
fun View.dp2px(value: Float): Int = context.dp2px(value)

/** Converts sp to px. Return the result as Int */
fun Context.sp2px(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()

/** Converts sp to px. Return the result as Int */
fun View.sp2px(value: Int): Int = context.sp2px(value)

/** Converts sp to px. Return the result as Int */
fun Context.sp2px(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()

/** Converts sp to px. Return the result as Int */
fun View.sp2px(value: Float): Int = context.sp2px(value)

/** Converts px to dp. Return the result as Int */
fun Context.px2dp(value: Int): Int = (value / resources.displayMetrics.density).toInt()

/** Converts px to dp. Return the result as Int */
fun View.px2dp(value: Int): Int = context.px2dp(value)

/** Converts px to sp. Return the result as Int */
fun Context.px2sp(value: Int): Int = (value / resources.displayMetrics.scaledDensity).toInt()

/** Converts px to sp. Return the result as Int */
fun View.px2sp(value: Int): Int = context.px2sp(value)

/** Retrieve a dimensional for a particular resource ID for use as a size in raw pixels. Return the result as Int */
fun Context.dimen(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)

/** Retrieve a dimensional for a particular resource ID for use as a size in raw pixels. Return the result as Int */
fun View.dimen(@DimenRes resource: Int): Int = context.dimen(resource)
