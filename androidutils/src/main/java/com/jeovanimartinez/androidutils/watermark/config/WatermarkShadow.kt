package com.jeovanimartinez.androidutils.watermark.config

import androidx.annotation.ColorInt

/**
 * Configuration to create a shadow for watermark.
 * @param radius Shadow blur radius.
 * @param dx Shadow offset for x-axis.
 * @param dy Shadow offset for y-axis.
 * @param shadowColor Color-int for the shadow, can be apply alpha for transparent shadow.
 * */
data class WatermarkShadow(
    val radius: Float,
    val dx: Float,
    val dy: Float,
    @ColorInt val shadowColor: Int
)
