package com.jeovanimartinez.androidutils.activity.config

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes

/**
 * Class for the configuration of activity TaskDescription.
 * * IMPORTANT: This class represents only a basic configuration to use when the app runs on Android Oreo (8.1 API 27) and earlier versions of Android.
 *
 * @param title Title for the current activity to show in the task description.
 * @param icon Icon to show (resource id), it must be a PNG image, if the resource id is an SVG, it will not be applied and the app icon will be shown.
 * @param color A color to override the theme's primary color, this color must be opaque.
 * */
data class TaskDescriptionConfig(@StringOrStringRes val title: Any, @DrawableRes val icon: Int, @ColorInt val color: Int)
