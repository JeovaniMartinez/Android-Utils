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
import com.jeovanimartinez.androidutils.extensions.context.typeAsDrawable
import com.jeovanimartinez.androidutils.extensions.context.typeAsString
import com.jeovanimartinez.androidutils.extensions.dimension.dp2pxF
import com.jeovanimartinez.androidutils.extensions.dimension.sp2pxF
import com.jeovanimartinez.androidutils.extensions.graphics.rotate
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

    /**
     * Draw a watermark inside the [bitmap].
     * @param context Context.
     * @param bitmap Bitmap where the watermark is to be drawn.
     * @param watermark [Watermark.Drawable] or [Watermark.Text] to draw inside the [bitmap].
     * */
    fun drawWatermark(context: Context, bitmap: Bitmap, watermark: Watermark) {
        validateWatermark(watermark)
        when (watermark) {
            is Watermark.Drawable -> drawDrawableWatermark(context, bitmap, watermark)
            is Watermark.Text -> drawTextWatermark(context, bitmap, watermark)
        }
    }

    /**
     * Draw a list of watermarks inside the [bitmap].
     * @param context Context.
     * @param bitmap Bitmap where the watermarks are to be drawn.
     * @param watermarkList List of [Watermark.Drawable] or [Watermark.Text] to draw inside the [bitmap].
     * */
    @Suppress("unused")
    fun drawWatermark(context: Context, bitmap: Bitmap, watermarkList: ArrayList<Watermark>) {
        watermarkList.forEach { drawWatermark(context, bitmap, it) }
    }

    /**
     * Validate the properties of the [watermark] object, and generate an [IllegalArgumentException] in case of finding incorrect values.
     * */
    private fun validateWatermark(watermark: Watermark) {
        require(watermark.opacity in 0f..1f) {
            "The opacity value for the watermark must be between 0 and 1, current value is ${watermark.opacity}"
        }
        if (watermark is Watermark.Drawable) {
            if (watermark.width != null) {
                require(watermark.width >= 1f) {
                    "Watermark width must be equal to or greater than 1, current value is ${watermark.width} "
                }
            }
            if (watermark.height != null) {
                require(watermark.height >= 1f) {
                    "Watermark height must be equal to or greater than 1, current value is ${watermark.height}"
                }
            }
        } else if (watermark is Watermark.Text) {
            require(watermark.textSize > 0) {
                "Watermark text size must be greater than 0, current value is ${watermark.textSize}"
            }
        }
    }

    /**
     * Draw a drawable watermark inside a bitmap.
     * @param context Context.
     * @param targetBitmap Bitmap where the watermark is to be drawn.
     * @param watermark [Watermark.Drawable] to draw inside the [targetBitmap].
     * */
    private fun drawDrawableWatermark(context: Context, targetBitmap: Bitmap, watermark: Watermark.Drawable) {

        log("Started process to draw a drawable watermark inside the bitmap")
        log("Target bitmap size: width = ${targetBitmap.width} height = ${targetBitmap.height}")

        val drawable = context.typeAsDrawable(watermark.drawable)!! // Get the drawable object

        // Set the initial size of the watermark
        var watermarkWidth = watermark.width ?: drawable.intrinsicWidth.toFloat()
        var watermarkHeight = watermark.height ?: drawable.intrinsicHeight.toFloat()

        // If use the intrinsic values, are validated that they converted to int are greater than zero
        require(watermarkWidth.toInt() > 0) { "Current drawable no has an intrinsic width, please specify the desired width" }
        require(watermarkHeight.toInt() > 0) { "Current drawable no has an intrinsic height, please specify the desired height" }

        // Initial offsets
        var dx = watermark.dx
        var dy = watermark.dy

        // Processing the values according to the measurement dimension
        log("Original watermark values = width: ${watermark.width} = $watermarkWidth | height: ${watermark.height} = $watermarkHeight")
        log("Watermark measurement dimension is ${watermark.measurementDimension}")
        when (watermark.measurementDimension) {
            Dimension.PX -> log("The original values are kept, final size: width = $watermarkWidth height = $watermarkHeight | dx = $dx dy = $dy")
            Dimension.DP -> {
                watermarkWidth = context.dp2pxF(watermarkWidth)
                watermarkHeight = context.dp2pxF(watermarkHeight)
                dx = context.dp2pxF(watermark.dx)
                dy = context.dp2pxF(watermark.dy)
                log("The watermark values are converted to DP, final size: width = $watermarkWidth height = $watermarkHeight | dx = $dx dy = $dy")
            }

            Dimension.SP -> {
                watermarkWidth = context.sp2pxF(watermarkWidth)
                watermarkHeight = context.sp2pxF(watermarkHeight)
                dx = context.sp2pxF(watermark.dx)
                dy = context.sp2pxF(watermark.dy)
                log("The watermark values are converted to SP, final size: width = $watermarkWidth height = $watermarkHeight | dx = $dx dy = $dy")
                logw("It is not recommended to use SP as the dimension of measure in a drawable watermark")
            }
        }

        val bitmapTmp: Bitmap? // Auxiliary in if it is necessary to rotate the watermark
        // The final watermark is generated as a bitmap
        val watermarkBitmap = if (watermark.rotation == 0f) {
            log("No need to rotate the watermark, rotation = ${watermark.rotation} degrees")
            drawable.toBitmap(watermarkWidth.toInt(), watermarkHeight.toInt(), Bitmap.Config.ARGB_8888)
        } else {
            log("Watermark is rotated ${watermark.rotation} degrees")
            // A temporary bitmap is created, it is rotated and assigned to the watermark bitmap
            bitmapTmp = drawable.toBitmap(watermarkWidth.toInt(), watermarkHeight.toInt(), Bitmap.Config.ARGB_8888)
            bitmapTmp.density = targetBitmap.density
            bitmapTmp.rotate(watermark.rotation)
        }

        // It must have the same density as the bitmap where it is going to be drawn, so that it is drawn with the correct size and scale
        watermarkBitmap.density = targetBitmap.density

        log("Watermark opacity = ${watermark.opacity}")

        // Paint object is created to apply the style
        val paint = Paint().apply {
            alpha = watermark.opacity.mapValue(0f, 1f, 0f, 255f).toInt()
            isAntiAlias = true // For best quality
        }

        // Calculate the position on the x and y axes

        val positionX = when (watermark.position) {
            ABSOLUTE -> -(watermarkBitmap.width / 2).toFloat()
            TOP_LEFT, MIDDLE_LEFT, BOTTOM_LEFT -> 0f
            TOP_CENTER, MIDDLE_CENTER, BOTTOM_CENTER -> ((targetBitmap.width / 2) - (watermarkBitmap.width / 2)).toFloat()
            TOP_RIGHT, MIDDLE_RIGHT, BOTTOM_RIGHT -> (targetBitmap.width - watermarkBitmap.width).toFloat()
        }

        val positionY = when (watermark.position) {
            ABSOLUTE -> -(watermarkBitmap.height / 2).toFloat()
            TOP_LEFT, TOP_CENTER, TOP_RIGHT -> 0f
            MIDDLE_LEFT, MIDDLE_CENTER, MIDDLE_RIGHT -> ((targetBitmap.height / 2) - (watermarkBitmap.height / 2)).toFloat()
            BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> (targetBitmap.height - watermarkBitmap.height).toFloat()
        }

        log("Watermark position = ${watermark.position} | Position in x-axis = $positionX | Position in y-axis = $positionY")

        // Calculate the final position
        val finalPositionX = positionX + dx
        val finalPositionY = positionY + dy

        // Draw the watermark in target bitmap
        val canvas = Canvas(targetBitmap)
        canvas.drawBitmap(watermarkBitmap, finalPositionX, finalPositionY, paint)

        log("Watermark drawn in target bitmap at x = $finalPositionX y = $finalPositionY")

        log("Watermark drawing done")

        // ** Prevent call watermarkBitmap.recycle(), in some cases generate an exception
    }

    /**
     * Draw a text watermark inside a bitmap.
     * @param context Context.
     * @param targetBitmap Bitmap where the watermark is to be drawn.
     * @param watermark [Watermark.Text] to draw in the [targetBitmap].
     * */
    private fun drawTextWatermark(context: Context, targetBitmap: Bitmap, watermark: Watermark.Text) {

        log("Started process to draw a text watermark inside the bitmap")

        val text = context.typeAsString(watermark.text) // Get the text

        // Gets the shadow for the watermark if it was defined, or generates one with no effect
        var shadow = if (watermark.shadow != null) {
            log("Use defined watermark shadow")
            watermark.shadow
        } else {
            log("Watermark shadow is null, no need to apply it")
            WatermarkShadow(0f, 0f, 0f, Color.TRANSPARENT)
        }

        // Initial values
        var dx = watermark.dx
        var dy = watermark.dy
        var textSize = watermark.textSize

        // Processing the values according to the measurement dimension
        log("The watermark measurement dimension is ${watermark.measurementDimension}")
        when (watermark.measurementDimension) {
            Dimension.PX -> {
                log("The original values are kept, text size = $textSize | dx = $dx dy = $dy")
            }

            Dimension.DP -> {
                textSize = context.dp2pxF(textSize)
                dx = context.dp2pxF(watermark.dx)
                dy = context.dp2pxF(watermark.dy)
                shadow = WatermarkShadow(
                    context.dp2pxF(shadow.radius),
                    context.dp2pxF(shadow.dx),
                    context.dp2pxF(shadow.dy),
                    shadow.color
                )
                log("The watermark values are converted to DP, text size = $textSize | dx = $dx dy = $dy")
            }

            Dimension.SP -> {
                textSize = context.sp2pxF(textSize)
                dx = context.sp2pxF(watermark.dx)
                dy = context.sp2pxF(watermark.dy)
                shadow = WatermarkShadow(
                    context.sp2pxF(shadow.radius),
                    context.sp2pxF(shadow.dx),
                    context.sp2pxF(shadow.dy),
                    shadow.color
                )
                log("The watermark values are converted to SP, text size = $textSize | dx = $dx dy = $dy")
                logw("It is not recommended to use SP as the dimension of measure in a text watermark")
            }
        }
        // The shadow values are also logged
        if (watermark.shadow != null) log("Shadow values: radius = ${shadow.radius} dx = ${shadow.dx} dy = ${shadow.dy}")

        // Opacity and rotation are rendered last, when it is drawn as a drawable watermark

        // Paint object is created to apply the style
        val paint = Paint().apply {
            color = watermark.textColor
            this.textSize = textSize
            textAlign = Paint.Align.LEFT
            isAntiAlias = true // For best quality
            watermark.typeface.whenNotNull { typeface = it }
            watermark.shadow.whenNotNull {
                // Use shadow instead of 'it' because shadow contains the processed values
                setShadowLayer(shadow.radius, shadow.dx, shadow.dy, shadow.color)
            }
        }

        val textWidth = paint.measureText(text) // The width that the text will occupy is calculated
        // For the height of the text, the height of the font must be considered, to contemplate all the characters that may appear
        val fontHeight = abs(paint.fontMetrics.ascent) + abs(paint.fontMetrics.descent) // Total height
        val fontHeightAscent = abs(paint.fontMetrics.ascent) // Height above line
        val fontHeightDescent = abs(paint.fontMetrics.descent) // Height below line

        log("textWidth = $textWidth | fontHeightAscent = $fontHeightAscent | fontHeightDescent = $fontHeightDescent | fontHeight = $fontHeight")

        // Absolute values of shadow offset
        val absDx = abs(shadow.dx)
        val absDy = abs(shadow.dy)

        // Create a bitmap for the text, considering the text size and the shadow
        val textBitmap = Bitmap.createBitmap((textWidth + absDx).toInt(), (fontHeight + absDy).toInt(), Bitmap.Config.ARGB_8888) // Create a bitmap for the text
        textBitmap.density = targetBitmap.density // To scale appropriately according to the bitmap.
        val textCanvas = Canvas(textBitmap) // Canvas for drawing the text

        // The internal offset of the text is calculated, according to the offset of the shadow
        val textOffsetX = if (shadow.dx < 0) absDx else 0f
        val textOffsetY = if (shadow.dy < 0) absDy else 0f

        // Draw the text, considering the offsets and font height
        textCanvas.drawText(text, textOffsetX, fontHeightAscent + textOffsetY, paint)

        log("A bitmap with the text watermark has been created, we proceed to draw it on the image using drawDrawableWatermark()")

        // Since the text watermark is done, it is drawn as a drawable
        drawDrawableWatermark(
            context,
            targetBitmap,
            Watermark.Drawable(
                textBitmap.toDrawable(context.resources),
                watermark.position,
                textBitmap.width.toFloat(),
                textBitmap.height.toFloat(),
                dx,
                dy,
                watermark.rotation,
                watermark.opacity,
                Dimension.PX // Always use PX as the values have already been processed
            )
        )

    }

}
