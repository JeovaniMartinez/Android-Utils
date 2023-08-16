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
 * Set of extensions to work with resources within the context.
 * */

/** Get a color from the resources based on their ID using ResourcesCompat.getColor() */
@ColorInt
fun Context.getColorCompat(@ColorRes id: Int): Int {
    return ResourcesCompat.getColor(this.resources, id, this.theme)
}

/** Get a Drawable from the resources based on their ID using AppCompatResources.getDrawable() */
fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? {
    return AppCompatResources.getDrawable(this, id)
}

/** Get a font from the resources based on their ID using ResourcesCompat.getFont() */
fun Context.getFontCompat(@FontRes id: Int): Typeface? {
    return ResourcesCompat.getFont(this, id)
}
