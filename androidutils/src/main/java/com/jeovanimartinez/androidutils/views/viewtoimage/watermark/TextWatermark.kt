package com.jeovanimartinez.androidutils.views.viewtoimage.watermark

import androidx.annotation.ColorInt
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes

/**
 * Configuración para crear una marca de agua de texto
 * @param text Texto.
 * @param textColor Color del texto.
 * @param position Posición del texto.
 * @param rotation Rotación del texto.
 * */
data class TextWatermark(
    @StringOrStringRes val text: Any,
    @ColorInt val textColor: Int,
    val position: WatermarkPosition,
    val rotation: Float = 0f
)
