package com.jeovanimartinez.androidutils.watermark.config

import androidx.annotation.ColorInt

/**
 * Configuration to create a shadow for a watermark.
 * @param radius Shadow blur radius.
 * @param dx Shadow offset for the x-axis.
 * @param dy Shadow offset for the y-axis.
 * @param shadowColor Color-int for the shadow, can be applied alpha for a transparent shadow.
 * */
data class WatermarkShadow(
    val radius: Float,
    val dx: Float,
    val dy: Float,
    @ColorInt val shadowColor: Int
)
