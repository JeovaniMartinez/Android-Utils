package com.jeovanimartinez.androidutils.watermark.type

import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.FontRes
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes
import com.jeovanimartinez.androidutils.watermark.config.WatermarkPosition
import com.jeovanimartinez.androidutils.watermark.config.WatermarkRotation
import com.jeovanimartinez.androidutils.watermark.config.WatermarkShadow

/**
 * Configuración para crear una marca de agua de texto.
 * @param text Texto para la marca de agua.
 * @param textSize Tamaño del texto. El tamaño del texto se interpreta en dp, por ejemplo si textSize = 12f, al generar la marca de agua es como
 *        si textSize fuera 12dp. Se interpreta como dp en lugar de sp para mantener el tamaño, ya que si el dispositivo usa un tamaño de fuente
 *        más grande o más pequeña, el tamaño del texto de la marca de agua variaría, y esto sería un inconveniente, ya que, por ejemplo, si se
 *        deja un margen en la imagen de 20dp para la marca de agua, y el tamaño del texto fuese de 20f (20sp), si el dispositivo muestra la fuente
 *        tamaño normal no habría problema, pero si usa una fuente más grande, el texto de la marca de agua no cabria en el margen, así que interpretando
 *        el valor como dp no se genera este inconveniente.
 * @param textColor Color-int del texto.
 * @param position Posición de la marca de agua dentro de la imagen.
 * @param offsetX Desplazamiento en el eje x, el valor se interpreta como dp, por ejemplo, si offsetX = 10f se interpreta como offsetX = 10dp.
 * @param offsetY Desplazamiento en el eje y, el valor se interpreta como dp, por ejemplo, si offsetY = 10f se interpreta como offsetY = 10dp.
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
    var rotation: Float,
    @FloatRange(from = 0.0, to = 1.0) val opacity: Float = 1f,
    @FontRes val typeface: Int? = null,
    val shadow: WatermarkShadow? = null
)
