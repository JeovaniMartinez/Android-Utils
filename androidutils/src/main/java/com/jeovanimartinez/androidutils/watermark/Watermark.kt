package com.jeovanimartinez.androidutils.watermark

import android.graphics.Color
import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import com.jeovanimartinez.androidutils.graphics.utils.Dimension
import com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes
import com.jeovanimartinez.androidutils.watermark.config.WatermarkPosition
import com.jeovanimartinez.androidutils.watermark.config.WatermarkShadow

/**
 * Definition class for a watermark.
 * */
sealed class Watermark {

    abstract val position: WatermarkPosition
    abstract val dx: Float
    abstract val dy: Float
    abstract val rotation: Float
    abstract val opacity: Float
    abstract val measurementDimension: Dimension

    /**
     * Definition for a drawable watermark (shape, image, etc.)
     * @param drawable Drawable resource or object for the watermark.
     * @param position Position for the watermark inside of the image.
     * @param width Width for the watermark (the minimum value is 1), if the value is null, the intrinsic width of the drawable will be used.
     *        If the drawable has no intrinsic width, such as a solid color, an exception will be thrown when drawing the watermark.
     * @param height Height for the watermark (the minimum value is 1), if the value is null, the intrinsic height of the drawable will be used.
     *        If the drawable has no intrinsic height, such as a solid color, an exception will be thrown when drawing the watermark.
     * @param dx Watermark offset for the x-axis.
     * @param dy Watermark offset for the y-axis.
     * @param rotation Rotation in degrees for the watermark.
     * @param opacity Opacity of the watermark. This is a value from 0 to 1, where 0 means the watermark is completely
     *        transparent and 1 means the watermark is completely opaque.
     * @param measurementDimension Type of dimension to use to draw watermark, its apply for width, height, dx, and dy.
     *        For example, if the value of width is 10f and measurementDimension is Dimension.PX, the width of the drawn watermark
     *        will be 10 px, but if measurementDimension is Dimension.DP, the width of the drawn watermark will be calculated
     *        according to the screen density. It is recommended to use only Dimension.PX or Dimension.DP for a Drawable watermark.
     * */
    data class Drawable(
        @DrawableOrDrawableRes val drawable: Any,
        override val position: WatermarkPosition = WatermarkPosition.MIDDLE_CENTER,
        @FloatRange(from = 1.0) val width: Float? = 1f,
        @FloatRange(from = 1.0) val height: Float? = 1f,
        override val dx: Float = 0f,
        override val dy: Float = 0f,
        override val rotation: Float = 0f,
        @FloatRange(from = 0.0, to = 1.0) override val opacity: Float = 1f,
        override val measurementDimension: Dimension = Dimension.PX
    ) : Watermark()

    /**
     * Definition for a text watermark.
     * @param text Text for the watermark.
     * @param textSize Text size for the watermark.
     * @param textColor Color-int for the watermark text.
     * @param position Position for the watermark inside of the image.
     * @param dx Watermark offset for the x-axis.
     * @param dy Watermark offset for the y-axis.
     * @param rotation Rotation in degrees for the watermark.
     * @param opacity Opacity of the watermark. This is a value from 0 to 1, where 0 means the watermark is completely
     *        transparent and 1 means the watermark is completely opaque.
     * @param typeface Custom font for the watermark text, if the value is null applies default font.
     * @param shadow Shadow configuration for the text watermark, null for not applying shadow.
     * @param measurementDimension Type of dimension to use to draw watermark, it applies for textSize, dx, dy, and shadow configuration (radius, dx, and dy).
     *        For example, if the value of dx is 10f and measurementDimension is Dimension.PX, the offset of the drawn watermark will be 10 px on the x-axis,
     *        but if measurementDimension is Dimension.DP, the offset of the drawn watermark on the x-axis will be calculated according to the screen density.
     *        It is recommended to use DP instead of SP to prevent the text size from being affected by the device's font size setting.
     * */
    data class Text(
        @StringOrStringRes val text: Any,
        val textSize: Float = 12f,
        @ColorInt val textColor: Int = Color.BLACK,
        override val position: WatermarkPosition = WatermarkPosition.MIDDLE_CENTER,
        override val dx: Float = 0f,
        override val dy: Float = 0f,
        override val rotation: Float = 0f,
        @FloatRange(from = 0.0, to = 1.0) override val opacity: Float = 1f,
        val typeface: Typeface? = null,
        val shadow: WatermarkShadow? = null,
        override val measurementDimension: Dimension = Dimension.PX
    ) : Watermark()

}
