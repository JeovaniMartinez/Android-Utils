package com.jeovanimartinez.androidutils.views.viewtoimage.watermark

import androidx.annotation.FloatRange
import com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes
import com.jeovanimartinez.androidutils.views.utils.Margin

/**
 * Configuración para crear una marca de un drawable (figura, forma, imagen, etc.)
 * @param drawable Drawable para la marca de agua.
 * @param position Posición del drawable dentro de la imagen.
 * @param margin Márgenes del drawable.
 * @param rotation Rotación del drawable.
 * @param opacity Opacidad del drawable en un valor de 0 a 1, donde 0 significa completamente transparente
 *        y 1 significa completamente opaco.
 * */
data class DrawableWatermark(
    @DrawableOrDrawableRes val drawable: Any?,
    val position: WatermarkPosition,
    val margin: Margin,
    val rotation: Float = 0f,
    @FloatRange(from = 0.0, to = 1.0) val opacity: Float = 1f,
)
