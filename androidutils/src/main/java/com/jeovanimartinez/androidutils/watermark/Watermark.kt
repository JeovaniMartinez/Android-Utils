package com.jeovanimartinez.androidutils.watermark

import android.graphics.Bitmap
import androidx.annotation.FloatRange
import com.jeovanimartinez.androidutils.Dimension
import com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes
import com.jeovanimartinez.androidutils.watermark.config.WatermarkPosition

/**
 * Definition class for a watermark.
 * */
sealed class Watermark {

    abstract val position: WatermarkPosition
    abstract val dx: Float
    abstract val dy: Float
    abstract val rotation: Float
    abstract val opacity: Float
    abstract val measureDimension: Dimension

    /**
     * Definition for a drawable watermark (shape, image, etc.)
     * @param drawable Drawable resource or object for the watermark.
     * @param position Position for the watermark inside of the image.
     * @param width Width for the watermark if the value is negative, the intrinsic width of the drawable will be used.
     *        If the drawable intrinsic width is also negative, an exception will be thrown.
     * @param height Height for the watermark if the value is negative, the intrinsic height of the drawable will be used.
     *        If the drawable intrinsic height is also negative, an exception will be thrown.
     * @param dx Watermark offset for the x-axis.
     * @param dy Watermark offset for the y-axis.
     * @param rotation Rotation in degrees for the watermark.
     * @param opacity Opacity of the watermark. This is a value from 0 to 1, where 0 means the view is completely
     *        transparent and 1 means the view is completely opaque.
     * @param measureDimension Type of dimension to use to draw watermark, its apply for width, height, dx and dy.
     *        For example, if the value of width is 10f and measureDimension is Dimension.PX, the width of the drawn watermark
     *        will be 10 px, but if measureDimension is Dimension.DP, the the width of the drawn watermark will be calculate
     *        according the screen density.
     * */
    data class Drawable(
        @DrawableOrDrawableRes val drawable: Any,
        override val position: WatermarkPosition,
        val width: Float = -1f,
        val height: Float = -1f,
        override val dx: Float = 0f,
        override val dy: Float = 0f,
        override val rotation: Float = 0f,
        @FloatRange(from = 0.0, to = 1.0) override val opacity: Float = 1f,
        override val measureDimension: Dimension = Dimension.PX
    ) : Watermark()

}
