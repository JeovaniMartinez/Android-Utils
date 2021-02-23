@file:Suppress("unused")

package com.jeovanimartinez.androidutils.watermark.config

/**
 * Enum for watermark position.
 * Use ABSOLUTE for custom position, or any other for fixed position.
 * */
enum class WatermarkPosition {
    ABSOLUTE,
    TOP_LEFT,
    TOP_CENTER,
    TOP_RIGHT,
    MIDDLE_LEFT,
    MIDDLE_CENTER,
    MIDDLE_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_CENTER,
    BOTTOM_RIGHT,
}
