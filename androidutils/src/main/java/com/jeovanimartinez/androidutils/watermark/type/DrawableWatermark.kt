package com.jeovanimartinez.androidutils.watermark.type

import androidx.annotation.Dimension
import androidx.annotation.FloatRange
import com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes
import com.jeovanimartinez.androidutils.watermark.Watermark
import com.jeovanimartinez.androidutils.watermark.config.WatermarkPosition

/**
 * Configuración para crear una marca de un drawable (figura, forma, imagen, etc.)
 * @param drawable Drawable para la marca de agua.
 * @param position Posición de la marca de agua dentro de la imagen.
 * @param width Ancho para el drawable y la marca de agua en dp.
 * @param height Alto para el drawable y la marca de agua en dp.
 * @param offsetX Desplazamiento en el eje x en dp.
 * @param offsetY Desplazamiento en el eje y en dp.
 * @param rotation Rotación en grados para la marca de agua.
 * @param opacity Opacidad de la marca de agua en un valor de 0 a 1, donde 0 significa
 *        completamente transparente y 1 significa completamente opaco.
 * */
data class DrawableWatermark(
    @DrawableOrDrawableRes val drawable: Any,
    val position: WatermarkPosition,
    @Dimension(unit = Dimension.DP) val width: Float,
    @Dimension(unit = Dimension.DP) val height: Float,
    @Dimension(unit = Dimension.DP) val offsetX: Float,
    @Dimension(unit = Dimension.DP) val offsetY: Float,
    val rotation: Float = 0f,
    @FloatRange(from = 0.0, to = 1.0) val opacity: Float = 1f,
) : Watermark
