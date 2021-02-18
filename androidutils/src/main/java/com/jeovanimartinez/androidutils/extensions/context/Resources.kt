@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.context

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat

/**
 * Conjunto de extensiones para trabajar con los recursos dentro del context.
 * */

/** Obtiene un color de los recursos en base a su id mediante ResourcesCompat.getColor() */
@ColorInt
fun Context.getColorCompat(@ColorRes id: Int): Int {
    return ResourcesCompat.getColor(this.resources, id, this.theme)
}

/** Obtiene un drawable de los recursos en base a su id mediante AppCompatResources.getDrawable() */
fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? {
    return AppCompatResources.getDrawable(this, id)
}

/** Obtiene una fuente de los recursos en base a su id mediante ResourcesCompat.getFont() */
fun Context.getFontCompat(@FontRes id: Int) : Typeface? {
    return ResourcesCompat.getFont(this, id)
}
