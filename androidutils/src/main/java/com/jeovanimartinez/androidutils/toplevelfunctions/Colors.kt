package com.jeovanimartinez.androidutils.toplevelfunctions

/**
 * Converts hexadecimal ARGB color to a hexadecimal RGBA color.
 * @param colorArgb String with the color in ARGB.
 * @return String with the color in RGBA.
 * @throws IllegalArgumentException If the ARGB color has a length different from 8 characters.
 * */
fun convertHexColorArgbToRgba(colorArgb: String): String {

    require(colorArgb.length == 8) {
        "The ARGB must be exactly 8 characters long, current is ${colorArgb.length} ($colorArgb)"
    }

    // It's just rearranging the string
    return colorArgb.substring(2, 8) + colorArgb.substring(0, 2)

}
