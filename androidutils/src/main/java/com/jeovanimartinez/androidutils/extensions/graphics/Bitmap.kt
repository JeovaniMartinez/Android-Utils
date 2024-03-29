@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.graphics

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import androidx.annotation.ColorInt
import com.jeovanimartinez.androidutils.graphics.utils.Margin

/**
 * Set of extensions for the Bitmap class.
 * */

/**
 * Rotate a bitmap the indicated degrees.
 * @param degrees Degrees that the bitmap is to be rotated.
 * @return A new bitmap with the rotation applied.
 * */
fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.setRotate(degrees, this.width.toFloat() / 2, this.height.toFloat() / 2)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

// Reference: https://stackoverflow.com/a/49281542
/**
 * Trims the borders of a bitmap of a specific color.
 * @param color Color of the borders to be trimmed.
 * @param trimMargin Optional margin in pixels to keep between the edges of the content and the bitmap borders.
 * @return A new bitmap with the borders trimmed.
 * */
fun Bitmap.trimByBorderColor(@ColorInt color: Int = Color.TRANSPARENT, trimMargin: Margin? = null): Bitmap {

    var top = height
    var bottom = 0
    var right = width
    var left = 0

    var colored = IntArray(width) { color }
    var buffer = IntArray(width)

    for (y in bottom until top) {
        getPixels(buffer, 0, width, 0, y, width, 1)
        if (!colored.contentEquals(buffer)) {
            bottom = y
            break
        }
    }

    for (y in top - 1 downTo bottom) {
        getPixels(buffer, 0, width, 0, y, width, 1)
        if (!colored.contentEquals(buffer)) {
            top = y
            break
        }
    }

    val heightRemaining = top - bottom
    colored = IntArray(heightRemaining) { color }
    buffer = IntArray(heightRemaining)

    for (x in left until right) {
        getPixels(buffer, 0, 1, x, bottom, 1, heightRemaining)
        if (!colored.contentEquals(buffer)) {
            left = x
            break
        }
    }

    for (x in right - 1 downTo left) {
        getPixels(buffer, 0, 1, x, bottom, 1, heightRemaining)
        if (!colored.contentEquals(buffer)) {
            right = x
            break
        }
    }

    return if (trimMargin != null) {
        val result = Bitmap.createBitmap(
            right - left + (trimMargin.left + trimMargin.right).toInt(),
            top - bottom + (trimMargin.top + trimMargin.bottom).toInt(),
            Bitmap.Config.ARGB_8888
        )
        Canvas(result).drawBitmap(this, -left.toFloat() + trimMargin.left, -bottom.toFloat() + trimMargin.top, null)
        result
    } else {
        Bitmap.createBitmap(this, left, bottom, right - left, top - bottom)
    }

}
