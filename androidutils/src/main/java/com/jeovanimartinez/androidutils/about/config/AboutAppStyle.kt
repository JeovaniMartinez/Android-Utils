package com.jeovanimartinez.androidutils.about.config

import androidx.annotation.ColorInt

/**
 * Configuration of the style for the about activity.
 * @param primaryColor Main color, used for progress bars, buttons, etc.
 * @param backgroundColor Color for the background.
 * @param textColor Color for the texts.
 * @param iconsColor Color for the icons.
 * */
data class AboutAppStyle(
    @param:ColorInt val primaryColor: Int,
    @param:ColorInt val backgroundColor: Int,
    @param:ColorInt val textColor: Int,
    @param:ColorInt val iconsColor: Int,
)
