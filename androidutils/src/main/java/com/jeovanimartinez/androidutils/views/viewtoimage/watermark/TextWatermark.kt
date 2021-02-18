package com.jeovanimartinez.androidutils.views.viewtoimage.watermark

import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.FontRes
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes

/**
 * Configuración para crear una marca de agua de texto.
 * @param text Texto para la marca de agua.
 * @param textSize Tamaño del texto.
 * @param textColor Color-int del texto.
 * @param position Posición de la marca de agua dentro de la imagen.
 * @param offsetX Desplazamiento en el eje x.
 * @param offsetY Desplazamiento en el eje y.
 * @param rotation Rotación del texto.
 * @param opacity Opacidad del texto en un valor de 0 a 1, donde 0 significa que el texto es completamente transparente
 *        y 1 significa que el texto es completamente opaco.
 * @param typeface Fuente para el texto, null aplica la fuente predeterminada.
 * */
data class TextWatermark(
    @StringOrStringRes val text: Any,
    val textSize: Float,
    @ColorInt val textColor: Int,
    val position: WatermarkPosition,
    val offsetX: Float,
    val offsetY: Float,
    var rotation: WatermarkRotation = WatermarkRotation.DEG_0,
    @FloatRange(from = 0.0, to = 1.0) val opacity: Float = 1f,
    @FontRes val typeface: Int? = null
)
