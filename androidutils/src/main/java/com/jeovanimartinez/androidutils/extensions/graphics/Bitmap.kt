@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.graphics

import android.graphics.Bitmap
import android.graphics.Matrix

/**
 * Conjunto de extensiones para la clase Bitmap
 * */

/**
 * Gira un mapa de bits.
 * @param degrees Grados que se va a rotar el bitmap.
 * @return Un nuevo bitmap idéntico al original con la rotación aplicada.
 * */
fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.setRotate(degrees, this.width.toFloat() / 2, this.height.toFloat() / 2)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}
