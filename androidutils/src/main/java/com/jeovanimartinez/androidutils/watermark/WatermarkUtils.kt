package com.jeovanimartinez.androidutils.watermark

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.graphics.drawable.toBitmap
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.extensions.basictypes.mapValue
import com.jeovanimartinez.androidutils.extensions.context.typeAsDrawable
import com.jeovanimartinez.androidutils.extensions.dimension.dp2px
import com.jeovanimartinez.androidutils.extensions.graphics.rotate
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull
import com.jeovanimartinez.androidutils.graphics.utils.Dimension
import com.jeovanimartinez.androidutils.watermark.config.WatermarkPosition.*

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
            is Watermark.Text -> {
            }
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

}
