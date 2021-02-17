package com.jeovanimartinez.androidutils.views.viewtoimage

import android.content.Context
import android.graphics.Bitmap
import com.jeovanimartinez.androidutils.Base

/**
 * Utilidad para convertir una vista en una imagen.
 * */
object ViewToImage : Base<ViewToImage>() {

    override val LOG_TAG = "ViewToImage"

    /**
     * Ejecuta la conversión de una vista a una imagen tipo Bitmap.
     * @param context Contexto.
     * @param config Objeto de configuración.
     * @return La vista en formato Bitmap.
     * */
    fun convert(context: Context, config: ViewToImageConfig): Bitmap {

        return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888) // Temporal

    }

}
