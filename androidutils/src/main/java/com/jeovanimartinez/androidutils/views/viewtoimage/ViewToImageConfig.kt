package com.jeovanimartinez.androidutils.views.viewtoimage

import android.content.Context
import android.view.View
import androidx.annotation.ColorInt
import com.jeovanimartinez.androidutils.views.viewtoimage.watermark.TextWatermark

/**
 * Configuración para convertir una vista en una imagen.
 * @param context Contexto.
 * @param view Vista que se va a convertir a imagen.
 * @param backgroundColor Color de fondo a aplicar a la imagen, null aplica un color transparente.
 * @param textWatermark Marca de agua de texto a colocar en la imagen.
 * */
data class ViewToImageConfig(
    val context: Context,
    val view: View,
    @ColorInt val backgroundColor: Int? = null,
    val textWatermark: TextWatermark? = null
)
