package com.jeovanimartinez.androidutils.views.viewtoimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.extensions.view.dp2px

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

        log("Converting view into bitmap image")

        val view = config.view

        // Se calcula el padding y el tamaño final de la imagen
        val finalPadding = config.padding.dp2px(context) // Se calculan los valores del padding
        val imageWidth = (view.width + finalPadding.left + finalPadding.right).toInt() // Ancho final de la imagen
        val imageHeight = (view.height + finalPadding.top + finalPadding.bottom).toInt() // Alto final de la imagen

        log("Image size: $imageHeight height * $imageWidth width")

        // Se genera un bitmap de la vista para dibujarse en la imagen final
        val viewBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888) // Se genera el bitmap con el mismo ancho y alto de la vista
        val viewCanvas = Canvas(viewBitmap) // Se genera un canvas para el bitmap
        view.draw(viewCanvas) // Se dibuja la vista en el canvas, y como el canvas se genera a partir del bitmap, hace que se dibuje también en el bitmap

        // Se genera la imagen final tipo Bitmap, ya incluyendo el tamaño de la vista y el padding
        val image = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image) // Canvas para la imagen final

        // Se dibuja el color de fondo
        canvas.drawColor(config.backgroundColor)
        // Se dibuja el bitmap de la vista en el canvas de la imagen final, incluyendo el padding
        canvas.drawBitmap(viewBitmap, finalPadding.left, finalPadding.top, null)
        
        return image

    }

}
