@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.dimensions

import android.content.Context
import android.view.View
import androidx.annotation.DimenRes

/**
 * Conjunto de extensiones y utilidades para manejar dimensiones en px, dp y sp.
 * El código es el de la biblioteca anko: https://github.com/Kotlin/anko/blob/master/anko/library/static/commons/src/main/java/Dimensions.kt
 * */

/** Convierte dp en px (dip2px) */
fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()

/** Convierte dp en px (dip2px) */
fun View.dip(value: Int): Int = context.dip(value)

/** Convierte dp en px (dip2px) */
fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()

/** Convierte dp en px (dip2px) */
fun View.dip(value: Float): Int = context.dip(value)

/** Convierte sp en px (sp2px) */
fun Context.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()

/** Convierte sp en px (sp2px) */
fun View.sp(value: Int): Int = context.sp(value)

/** Convierte sp en px (sp2px) */
fun Context.sp(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()

/** Convierte sp en px (sp2px) */
fun View.sp(value: Float): Int = context.sp(value)

/** Convierte px en dp (px2dp) */
fun Context.px2dip(px: Int): Float = px.toFloat() / resources.displayMetrics.density

/** Convierte px en dp (px2dp) */
fun View.px2dip(px: Int): Float = context.px2dip(px)

/** Convierte px en sp (px2sp) */
fun Context.px2sp(px: Int): Float = px.toFloat() / resources.displayMetrics.scaledDensity

/** Convierte px en sp (px2sp) */
fun View.px2sp(px: Int): Float = context.px2sp(px)

/** Recuperar una dimensión para un ID de recurso en particular para su uso como tamaño en píxeles sin procesar */
fun Context.dimen(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)

/** Recuperar una dimensión para un ID de recurso en particular para su uso como tamaño en píxeles sin procesar */
fun View.dimen(@DimenRes resource: Int): Int = context.dimen(resource)
