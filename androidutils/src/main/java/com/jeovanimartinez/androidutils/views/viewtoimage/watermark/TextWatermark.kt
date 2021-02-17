package com.jeovanimartinez.androidutils.views.viewtoimage.watermark

import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes
import com.jeovanimartinez.androidutils.views.utils.Margin

/**
 * Configuración para crear una marca de agua de texto
 * @param text Texto.
 * @param textColor Color del texto.
 * @param position Posición del texto dentro de la imagen.
 * @param margin Márgenes para el texto.
 * @param rotation Rotación del texto.
 * @param opacity Opacidad del texto en un valor de 0 a 1, donde 0 significa que el texto es completamente transparente
 *        y 1 significa que el texto es completamente opaco.
 * */
data class TextWatermark(
    @StringOrStringRes val text: Any,
    @ColorInt val textColor: Int,
    val position: WatermarkPosition,
    val margin: Margin,
    val rotation: Float = 0f,
    @FloatRange(from = 0.0, to = 1.0) val opacity: Float = 1f,
)
