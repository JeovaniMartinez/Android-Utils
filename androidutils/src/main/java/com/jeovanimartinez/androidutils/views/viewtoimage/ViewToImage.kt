package com.jeovanimartinez.androidutils.views.viewtoimage

import android.content.Context
import android.graphics.*
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.extensions.context.getFontCompat
import com.jeovanimartinez.androidutils.extensions.context.typeAsString
import com.jeovanimartinez.androidutils.extensions.dimension.dp2px
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull
import com.jeovanimartinez.androidutils.extensions.view.dp2px
import com.jeovanimartinez.androidutils.views.viewtoimage.watermark.TextWatermark
import com.jeovanimartinez.androidutils.views.viewtoimage.watermark.WatermarkPosition.*
import com.jeovanimartinez.androidutils.views.viewtoimage.watermark.WatermarkRotation.*
import kotlin.math.abs

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

        // Se dibujan todas las marcas de agua de texto
        config.textWatermarkList.forEach { drawTextWatermark(context, canvas, it) }

        return image

    }

    /**
     * Dibuja la marca de agua de texto en el canvas.
     * @param context Contexto.
     * @param canvas Canvas de la imagen donde se va a dibujar la marca de agua.
     * @param watermark Marca de agua de texto.
     * */
    private fun drawTextWatermark(context: Context, canvas: Canvas, watermark: TextWatermark) {

        val text = context.typeAsString(watermark.text) // Texto para dibujar

        // Se genera un objeto Paint para aplicar el estilo
        val paint = Paint().apply {
            color = watermark.textColor
            textSize = context.dp2px(watermark.textSize).toFloat() // Se usa en dp, para evitar alteraciones si el dispositivo usa una fuente más grande o más pequeña
            isAntiAlias = true /// Para una buena calidad
            textAlign = Paint.Align.LEFT // Se alinea siempre a la izquierda y a partir de esa alineación se ajusta según la posición
            // Se asigna la fuente en caso de recibirla
            watermark.typeface.whenNotNull {
                typeface = context.getFontCompat(it)
            }
        }


        // Para el ancho que va a ocupar el texto, paint.measureText es más exacto que paint.getTextBounds, referencia: https://stackoverflow.com/a/7579469
        val textWidth = paint.measureText(text) // Ancho del texto

        // Para el alto del texto, se debe considerar el alto de la fuente, para contemplar todos los caracteres que pueden aparecer

        /*
        * fontMetrics del objeto paint indica métricas sobre la fuente, donde se puede deducir los siguiente:
        * Supongamos una linea imaginaria (renglón) donde se va a dibujar el texto, el texto queda por encima del renglón, pero los caracteres como la
        * "y" tiene una parte que queda por debajo del renglón. La parte que queda encima del renglón es el valor de ascent (que es negativo porque es
        * del renglón hacia arriba) y la parte que queda por debajo del renglón es descent, y sumando el valor absoluto de estos valores, podemos determinar
        * el alto máximo que puede usar la fuente para dibujar el texto. En algunos casos, es necesario usar solo el valor absoluto de ascent cuando se
        * dibuja en el canvas, ya que en el eje Y se comienza a dibujar en el renglón (excluyendo el espacio que ocupan los caracteres debajo del renglón) y en
        * otros casos se requiere usar solo el valor absoluto de descent, para que se alcance a apreciar el texto port debajo del renglón.
        * Referencia: https://stackoverflow.com/a/42091739
        * */

        val fontHeight = abs(paint.fontMetrics.ascent) + abs(paint.fontMetrics.descent) // Alto total de la fuente
        val fontHeightAscent = abs(paint.fontMetrics.ascent) // Alto por encima del renglón que puede ocupar la fuente
        val fontHeightDescent = abs(paint.fontMetrics.descent) // Alto por debajo del renglón que puede ocupar la fuente

        // Se obtienen datos de la configuración
        val position = watermark.position
        val rotation = watermark.rotation
        val offsetX = watermark.offsetX
        val offsetY = watermark.offsetY

        /*
        * Ll archivo view-to-image-helper.svg de los recursos del proyecto se uso para analizar y definir como generar la marca de agua
        * en las diferentes posiciones y en los diferentes ángulos.
        * */

        canvas.save() // Se guarda el estado actual del canvas

        // Se determina la posición inicial en el eje Y de acuerdo a la posición de la marca de agua (superior, a la mitad o inferior)
        val positionY = when (position) {
            TOP_LEFT, TOP_CENTER, TOP_RIGHT -> {
                0f
            }
            MIDDLE_LEFT, MIDDLE_CENTER, MIDDLE_RIGHT -> {
                if (rotation == DEG_0 || rotation == DEG_180) (canvas.height / 2) - (fontHeight / 2)
                else (canvas.height / 2) - (textWidth / 2)
            }
            BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> {
                if (rotation == DEG_0 || rotation == DEG_180) canvas.height - fontHeight
                else canvas.height - textWidth
            }
        }

        // Se determina la posición inicial en el eje X de acuerdo a la posición de la marca de agua (izquierda, centro o derecha)
        val positionX = when (position) {
            TOP_LEFT, MIDDLE_LEFT, BOTTOM_LEFT -> {
                0f
            }
            TOP_CENTER, MIDDLE_CENTER, BOTTOM_CENTER -> {
                if (rotation == DEG_0 || rotation == DEG_180) (canvas.width / 2) - (textWidth / 2)
                else (canvas.width / 2) - (fontHeight / 2)
            }
            TOP_RIGHT, MIDDLE_RIGHT, BOTTOM_RIGHT -> {
                if (rotation == DEG_0 || rotation == DEG_180) canvas.width - textWidth
                else canvas.width - fontHeight
            }
        }

        // Se ajusta la rotación del canvas según los grados y se dibuja el texto aplicando la configuración correspondiente
        when (rotation) {
            DEG_0 -> {
                canvas.drawText(text, positionX + offsetX, positionY + offsetY + fontHeightAscent, paint)
            }
            DEG_90 -> {
                // Al girar 90 grados, se intercambian los valores del eje X por los del eje Y y viceversa
                canvas.rotate(90f, 0f, 0f)
                canvas.drawText(text, positionY + offsetY, -positionX - offsetX - fontHeightDescent, paint)
            }
            DEG_180 -> {
                // Al rotar 180 grados se realizan restas para ajustar la posición del texto
                canvas.rotate(180f, 0f, 0f)
                canvas.drawText(text, -positionX - offsetX - textWidth, -positionY - offsetY - fontHeightDescent, paint)
            }
            DEG_270 -> {
                // Al girar 270 grados, se intercambian los valores del eje X por los del eje Y y viceversa
                canvas.rotate(270f, 0f, 0f)
                canvas.drawText(text, -positionY - offsetY - textWidth, positionX + offsetX + fontHeightAscent, paint)
            }
        }

        canvas.restore() // Se restablece el canvas para anular los cambios en la rotación después de dibujar el texto

    }

}
































