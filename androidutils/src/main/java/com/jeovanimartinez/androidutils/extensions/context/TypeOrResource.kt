@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.context

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes
import java.lang.Exception

/**
 * Set of extensions to parse a value as a data type, or to get the value from resources.
 * */

/**
 * Parses the received data type [stringOrStringRes] and always returns a String.
 * - If the data type is String, it returns it as is, without processing.
 * - If the data type is char, it returns it as a String.
 * - If the data type is an integer, it is assumed to be a resource ID, so the String is obtained from resources and returned. If the resource does not exist, it throws an exception.
 * - For any another data type, it throws an exception, since the data type cannot be parse directly as a String.
 * */
fun Context.typeAsString(@StringOrStringRes stringOrStringRes: Any): String {
    return when (stringOrStringRes) {
        is String -> stringOrStringRes
        is Char -> stringOrStringRes.toString()
        is Int -> this.getString(stringOrStringRes)
        else -> throw Exception("Expected value type String object or Int (for get the string resource by ID) but received value type ${stringOrStringRes.javaClass}")
    }
}

/**
 * Parses the received data type [drawableOrDrawableRes] and always returns a Drawable.
 * - If the data type is null, it returns null.
 * - If the data type is a Drawable, it returns it as is, without processing.
 * - If the data type is an integer, it is assumed to be a resource ID, so the Drawable is obtained from resources and returned. If the resource does not exist, it throws an exception.
 * - For any another data type, it throws an exception, since the data type cannot be parse as a Drawable.
 *
 * The parameter and return value can be null according to the definition of AppCompatResources.getDrawable()
 * */
fun Context.typeAsDrawable(@DrawableOrDrawableRes drawableOrDrawableRes: Any?): Drawable? {
    if (drawableOrDrawableRes == null) return null
    return when (drawableOrDrawableRes) {
        is Drawable -> drawableOrDrawableRes
        is Int -> AppCompatResources.getDrawable(this, drawableOrDrawableRes)
        else -> throw Exception("Expected value type Drawable object or Int (for get the drawable resource by ID) but received value type ${drawableOrDrawableRes.javaClass}")
    }
}
