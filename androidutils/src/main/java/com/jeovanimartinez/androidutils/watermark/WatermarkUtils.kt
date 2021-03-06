package com.jeovanimartinez.androidutils.watermark

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.extensions.basictypes.mapValue
import com.jeovanimartinez.androidutils.extensions.context.getFontCompat
import com.jeovanimartinez.androidutils.extensions.context.typeAsDrawable
import com.jeovanimartinez.androidutils.extensions.context.typeAsString
import com.jeovanimartinez.androidutils.extensions.dimension.dp2px
import com.jeovanimartinez.androidutils.extensions.graphics.rotate
import com.jeovanimartinez.androidutils.extensions.graphics.trimByBorderColor
import com.jeovanimartinez.androidutils.extensions.nullability.isNotNull
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull
import com.jeovanimartinez.androidutils.graphics.utils.Dimension
import com.jeovanimartinez.androidutils.watermark.config.WatermarkPosition.*
import com.jeovanimartinez.androidutils.watermark.config.WatermarkShadow
import kotlin.math.abs

/**
 * Utility class for draw a watermarks.
 * */
object WatermarkUtils : Base<WatermarkUtils>() {

    override val LOG_TAG = "WatermarkUtils"

    fun drawWatermarks(context: Context, bitmap: Bitmap, watermarkList: ArrayList<Watermark>) {
        watermarkList.forEach { drawWatermark(context, bitmap, it) }
    }

    fun drawWatermark(context: Context, bitmap: Bitmap, watermark: Watermark) {
        validateWatermark(watermark)
        when (watermark) {
            is Watermark.Drawable -> drawDrawableWatermark(context, bitmap, watermark)
            is Watermark.Text -> drawTextWatermark(context, bitmap, watermark)
        }
    }

    private fun validateWatermark(watermark: Watermark) {
        require(watermark.opacity in 0f..1f) {
            "Opacity value for the watermark must be between 0 and 1, current value is ${watermark.opacity}"
        }
        require(watermark.measurementDimension == Dimension.PX || watermark.measurementDimension == Dimension.DP) {
            "Watermark measurement dimension only accept Dimension.PX or Dimension.DP, current dimension is Dimension.${watermark.measurementDimension} "
        }
        if (watermark is Watermark.Drawable) {
            require(watermark.width >= 0f) {
                "Watermark width must be equal or greater than 0, current value is ${watermark.width} "
            }
            require(watermark.height >= 0f) {
                "Watermark height must be equal or greater than 0, current value is ${watermark.height}"
            }
        }
    }

    private fun drawDrawableWatermark(context: Context, baseBitmap: Bitmap, watermark: Watermark.Drawable) {

        val drawable = context.typeAsDrawable(watermark.drawable)!!

        var watermarkWidth = if (watermark.width == 0f) drawable.intrinsicWidth else watermark.width.toInt()
        var watermarkHeight = if (watermark.width == 0f) drawable.intrinsicHeight else watermark.height.toInt()

        require(watermarkWidth > 0) { "Current drawable no has an intrinsic width, please specify the desired width" }
        require(watermarkHeight > 0) { "Current drawable no has an intrinsic height, please specify the desired height" }

        if (watermark.measurementDimension == Dimension.DP) {
            watermarkWidth = context.dp2px(watermarkWidth)
            watermarkHeight = context.dp2px(watermarkHeight)
        }

        var bitmapTmp: Bitmap? = null
        val bitmap = if (watermark.rotation == 0f) {
            drawable.toBitmap(watermarkWidth, watermarkHeight, Bitmap.Config.ARGB_8888)
        } else {
            bitmapTmp = drawable.toBitmap(watermarkWidth, watermarkHeight, Bitmap.Config.ARGB_8888)
            bitmapTmp.rotate(watermark.rotation)
        }
        bitmapTmp.whenNotNull { it.recycle() }

        val paint = Paint().apply {
            alpha = watermark.opacity.mapValue(0f, 1f, 0f, 255f).toInt()
            isAntiAlias = true
        }

        val positionX = when (watermark.position) {
            ABSOLUTE -> -(watermarkWidth / 2).toFloat()
            TOP_LEFT, MIDDLE_LEFT, BOTTOM_LEFT -> 0f
            TOP_CENTER, MIDDLE_CENTER, BOTTOM_CENTER -> ((baseBitmap.width / 2) - (bitmap.width / 2)).toFloat()
            TOP_RIGHT, MIDDLE_RIGHT, BOTTOM_RIGHT -> (baseBitmap.width - bitmap.width).toFloat()
        }

        val positionY = when (watermark.position) {
            ABSOLUTE -> -(watermarkHeight / 2).toFloat()
            TOP_LEFT, TOP_CENTER, TOP_RIGHT -> 0f
            MIDDLE_LEFT, MIDDLE_CENTER, MIDDLE_RIGHT -> ((baseBitmap.height / 2) - (bitmap.height / 2)).toFloat()
            BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> (baseBitmap.height - bitmap.height).toFloat()
        }

        val dx = if (watermark.measurementDimension == Dimension.DP) context.dp2px(watermark.dx).toFloat() else watermark.dx
        val dy = if (watermark.measurementDimension == Dimension.DP) context.dp2px(watermark.dy).toFloat() else watermark.dy

        val canvas = Canvas(baseBitmap)
        canvas.drawBitmap(bitmap, positionX + dx, positionY + dy, paint)

        // Prevent call bitmap.recycle(), in some cases generate an exception
    }

    private fun drawTextWatermark(context: Context, baseBitmap: Bitmap, watermark: Watermark.Text) {

        val text = context.typeAsString(watermark.text)

        var watermarkShadow = watermark.shadow ?: WatermarkShadow(0f, 0f, 0f, Color.TRANSPARENT)

        if (watermark.measurementDimension == Dimension.DP && watermark.shadow.isNotNull()) {
            watermarkShadow = WatermarkShadow(
                context.dp2px(watermarkShadow.radius).toFloat(),
                context.dp2px(watermarkShadow.dx).toFloat(),
                context.dp2px(watermarkShadow.dy).toFloat(),
                watermarkShadow.shadowColor
            )
        }

        val paint = Paint().apply {
            color = watermark.textColor
            textSize = if (watermark.measurementDimension == Dimension.DP) context.dp2px(watermark.textSize).toFloat() else watermark.textSize
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
            alpha = watermark.opacity.mapValue(0f, 1f, 0f, 255f).toInt()
            watermark.typeface.whenNotNull {
                typeface = context.getFontCompat(it)
            }
            setShadowLayer(watermarkShadow.radius, watermarkShadow.dx, watermarkShadow.dy, watermarkShadow.shadowColor)
        }

        val textWidth = paint.measureText(text)

        val fontHeight = abs(paint.fontMetrics.ascent) + abs(paint.fontMetrics.descent)
        val fontHeightAscent = abs(paint.fontMetrics.ascent)
        val fontHeightDescent = abs(paint.fontMetrics.descent)

        val absDx = abs(watermarkShadow.dx)
        val absDy = abs(watermarkShadow.dy)

        val textBitmapWidth = textWidth.toInt() + (absDx * 2).toInt() + 50
        val textBitmapHeight= fontHeight.toInt() + (absDy * 2).toInt() + 50

        val textBitmap = Bitmap.createBitmap(textBitmapWidth, textBitmapHeight, Bitmap.Config.ARGB_8888)
        val textCanvas = Canvas(textBitmap)

        textCanvas.drawText(text, absDx, fontHeightAscent + absDy, paint)

        val finalTextBitmap = textBitmap.trimByBorderColor()

        val c = Canvas(baseBitmap)
        c.drawText(text, 10f, fontHeightAscent + 10, paint)

        val dx = if (watermark.measurementDimension == Dimension.DP) context.dp2px(watermark.dx).toFloat() else watermark.dx
        val dy = if (watermark.measurementDimension == Dimension.DP) context.dp2px(watermark.dy).toFloat() else watermark.dy

        drawDrawableWatermark(
            context,
            baseBitmap,
            Watermark.Drawable(
                finalTextBitmap.toDrawable(context.resources),
                watermark.position,
                finalTextBitmap.width.toFloat(),
                finalTextBitmap.height.toFloat(),
                dx,
                dy,
                watermark.rotation,
                1f,
                Dimension.PX
            )
        )

    }

//
//        // Se valida el valor de la opacidad
//        if (watermark.opacity < 0f || watermark.opacity > 1f) throw Exception("The opacity value for text watermark must be between 0 and 1")
//
//        val watermarkShadow = if (watermark.shadow.isNotNull()) watermark.shadow!! else WatermarkShadow(0f, 0f, 0f, Color.TRANSPARENT)
//
//        // Se genera un objeto Paint para aplicar el estilo
//        val paint = Paint().apply {
//            color = watermark.textColor
//            textSize = context.dp2px(watermark.textSize).toFloat() // Se usa en dp, para evitar alteraciones si el dispositivo usa una fuente más grande o más pequeña
//            isAntiAlias = true /// Para una buena calidad
//            textAlign = Paint.Align.LEFT // Se alinea siempre a la izquierda y a partir de esa alineación se ajusta según la posición
//            alpha = watermark.opacity.mapValue(0f, 1f, 0f, 255f).toInt() // Se mapea y asigna la opacidad
//            // Se asigna la fuente en caso de recibirla
//            watermark.typeface.whenNotNull {
//                typeface = context.getFontCompat(it)
//            }
//            setShadowLayer(watermarkShadow.radius, watermarkShadow.dx, watermarkShadow.dy, watermarkShadow.shadowColor)
//        }
//
//        // Para el ancho que va a ocupar el texto, paint.measureText es más exacto que paint.getTextBounds, referencia: https://stackoverflow.com/a/7579469
//        val textWidth = paint.measureText(text) // Ancho del texto
//
//        // Para el alto del texto, se debe considerar el alto de la fuente, para contemplar todos los caracteres que pueden aparecer
//
//        /*
//        * fontMetrics del objeto paint indica métricas sobre la fuente, donde se puede deducir los siguiente:
//        * Supongamos una linea imaginaria (renglón) donde se va a dibujar el texto, el texto queda por encima del renglón, pero los caracteres como la
//        * "y" tiene una parte que queda por debajo del renglón. La parte que queda encima del renglón es el valor de ascent (que es negativo porque es
//        * del renglón hacia arriba) y la parte que queda por debajo del renglón es descent, y sumando el valor absoluto de estos valores, podemos determinar
//        * el alto máximo que puede usar la fuente para dibujar el texto. En algunos casos, es necesario usar solo el valor absoluto de ascent cuando se
//        * dibuja en el canvas, ya que en el eje Y se comienza a dibujar en el renglón (excluyendo el espacio que ocupan los caracteres debajo del renglón) y en
//        * otros casos se requiere usar solo el valor absoluto de descent, para que se alcance a apreciar el texto port debajo del renglón.
//        * Referencia: https://stackoverflow.com/a/42091739
//        * */
//
//        val fontHeight = abs(paint.fontMetrics.ascent) + abs(paint.fontMetrics.descent) // Alto total de la fuente
//        val fontHeightAscent = abs(paint.fontMetrics.ascent) // Alto por encima del renglón que puede ocupar la fuente
//        val fontHeightDescent = abs(paint.fontMetrics.descent) // Alto por debajo del renglón que puede ocupar la fuente
//
//        // Se obtienen datos de la configuración
//        val rotation = watermark.rotation
//        val offsetX = context.dp2px(watermark.offsetX)
//        val offsetY = context.dp2px(watermark.offsetY)
//
//        /*
//        * El archivo view-to-image-helper.svg de los recursos del proyecto se uso para analizar y definir como generar la marca de agua
//        * en las diferentes posiciones y en los diferentes ángulos.
//        * */
//
//        val absX = abs(watermarkShadow.dx)
//        val absY = abs(watermarkShadow.dy)
//
//        val textBitmap = Bitmap.createBitmap(textWidth.toInt() + (absX * 2).toInt(), fontHeight.toInt() + (absY * 2).toInt(), Bitmap.Config.ARGB_8888) // Se genera un bitmap para dibujar el texto
//        val textCanvas = Canvas(textBitmap)
//
//        textCanvas.drawColor(Color.RED)
//        textCanvas.drawText(text, absX, fontHeightAscent + absY, paint) // Se dibuja el texto en el bitmap mediante el canvas
//
//
//        val trimed = textBitmap.trimByEdgeColor(Color.RED)
//
//        drawDrawableWatermark(
//            context,
//            canvas,
//            DrawableWatermark(
//                trimed.toDrawable(context.resources),
//                watermark.position, trimed.width.toFloat(),
//                trimed.height.toFloat(),
//                watermark.offsetX,
//                watermark.offsetY,
//                watermark.rotation,
//                1f // Se pasa siempre la opacidad de 1f, ya que este valor se aplicó en el paint del texto y no es necesario aplicarlo nuevamente
//            )
//        )
//
//        textBitmap.recycle()


}
