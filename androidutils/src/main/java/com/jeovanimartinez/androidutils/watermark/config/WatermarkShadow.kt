package com.jeovanimartinez.androidutils.watermark.config

import androidx.annotation.ColorInt

data class WatermarkShadow(
    val radius: Float,
    val dx: Float,
    val dy: Float,
    @ColorInt val shadowColor: Int
)
