package com.jeovanimartinez.androidutils.views.viewtoimage

import android.graphics.Color
import android.view.View
import androidx.annotation.ColorInt
import com.jeovanimartinez.androidutils.views.utils.Padding
import com.jeovanimartinez.androidutils.views.viewtoimage.watermark.DrawableWatermark
import com.jeovanimartinez.androidutils.views.viewtoimage.watermark.TextWatermark

/**
 * Configuración para convertir una vista en una imagen.
 * @param view Vista que se va a convertir a imagen.
 * @param backgroundColor Color de fondo a aplicar a la imagen, el predeterminado es transparente.
 * @param padding Padding a aplicar entre la vista y los bordes de la imagen. Los valores se interpretan como dp,
 *        por ejemplo si el padding top = 10.0, al generar la imagen el padding top será de 10dp.
 * @param textWatermarkList Lista de marcas de agua de texto, dejar la lista vacía para no aplicar ninguna.
 * @param drawableWatermarkList Lista de marcas de agua de drawable, dejar la lista vacía para no aplicar ninguna.
 * */
data class ViewToImageConfig(
    val view: View,
    @ColorInt val backgroundColor: Int = Color.TRANSPARENT,
    val padding: Padding,
    val textWatermarkList: ArrayList<TextWatermark> = arrayListOf(),
    val drawableWatermarkList: ArrayList<DrawableWatermark> = arrayListOf()
)
