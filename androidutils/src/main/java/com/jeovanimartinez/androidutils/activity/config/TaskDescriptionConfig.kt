package com.jeovanimartinez.androidutils.activity.config

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes

/**
 * Class for configuration of activity TaskDescription.
 *
 * @param title Title for the task description, String or string resource ID.
 * @param icon Icon to show (resource id), it must be a PNG image, if the resource id is SVG, it will not be applied and the app icon will be shown.
 * @param color Color-int for the background.
 * */
data class TaskDescriptionConfig(@StringOrStringRes val title: Any, @DrawableRes val icon: Int, @ColorInt val color: Int)
