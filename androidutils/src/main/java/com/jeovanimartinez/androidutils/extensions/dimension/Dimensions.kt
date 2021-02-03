@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.dimension

import android.content.Context
import android.view.View
import androidx.annotation.DimenRes

/**
 * Conjunto de extensiones y utilidades para manejar dimensiones en px, dp y sp.
 * El código es el de la biblioteca anko: https://github.com/Kotlin/anko/blob/master/anko/library/static/commons/src/main/java/Dimensions.kt
 * */

/** Convierte dp en px */
fun Context.dp2px(value: Int): Int = (value * resources.displayMetrics.density).toInt()

/** Convierte dp en px */
fun View.dp2px(value: Int): Int = context.dp2px(value)

/** Convierte dp en px */
fun Context.dp2px(value: Float): Int = (value * resources.displayMetrics.density).toInt()

/** Convierte dp en px */
fun View.dp2px(value: Float): Int = context.dp2px(value)

/** Convierte sp en px */
fun Context.sp2px(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()

/** Convierte sp en px */
fun View.sp2px(value: Int): Int = context.sp2px(value)

/** Convierte sp en px */
fun Context.sp2px(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()

/** Convierte sp en px */
fun View.sp2px(value: Float): Int = context.sp2px(value)

/** Convierte px en dp */
fun Context.px2dp(value: Int): Float = value.toFloat() / resources.displayMetrics.density

/** Convierte px en dp */
fun View.px2dp(value: Int): Float = context.px2dp(value)

/** Convierte px en sp */
fun Context.px2sp(value: Int): Float = value.toFloat() / resources.displayMetrics.scaledDensity

/** Convierte px en sp */
fun View.px2sp(value: Int): Float = context.px2sp(value)

/** Recuperar una dimensión para un ID de recurso en particular para su uso como tamaño en píxeles sin procesar */
fun Context.dimen(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)

/** Recuperar una dimensión para un ID de recurso en particular para su uso como tamaño en píxeles sin procesar */
fun View.dimen(@DimenRes resource: Int): Int = context.dimen(resource)
