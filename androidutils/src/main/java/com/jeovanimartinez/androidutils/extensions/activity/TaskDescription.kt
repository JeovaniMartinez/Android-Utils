@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.activity

import android.app.Activity
import android.app.ActivityManager.TaskDescription
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.jeovanimartinez.androidutils.activity.config.TaskDescriptionConfig
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes
import com.jeovanimartinez.androidutils.extensions.context.typeAsString

/**
 * Extensions to configure activities TaskDescription.
 * */

/**
 * Configure the activity TaskDescription.
 * * IMPORTANT: This configuration only applies if the app is running on Android Oreo (8.1 API 27) or earlier versions of Android.
 *   This is because in newer versions, when viewing the list of running apps, they are displayed differently, and the default Task Description
 *   settings looks good without the need for adjustments.
 *
 * @param title Title for the current activity to show in the task description.
 * @param icon Icon to show (resource id), it must be a PNG image, if the resource id is an SVG, it will not be applied and the app icon will be shown.
 * @param color A color to override the theme's primary color, this color must be opaque.
 * */
fun Activity.configureTaskDescription(@StringOrStringRes title: Any, @DrawableRes icon: Int, @ColorInt color: Int) {

    // From Android Lollipop (5.0 API 21, minSdkVersion of the library) to Android Oreo (8.1 API 27)
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {

        val finalTitle = typeAsString(title)

        @Suppress("DEPRECATION")
        val taskDescription = TaskDescription(finalTitle, BitmapFactory.decodeResource(resources, icon), color)
        this.setTaskDescription(taskDescription)

    }

}

/**
 * Configure the activity TaskDescription.
 * * IMPORTANT: This configuration only applies if the app is running on Android Oreo (8.1 API 27) or earlier versions of Android.
 *   This is because in newer versions, when viewing the list of running apps, they are displayed differently, and the default Task Description
 *   settings looks good without the need for adjustments.
 *
 * @param taskDescriptionConfig Object with the configuration for the activity TaskDescription.
 * */
fun Activity.configureTaskDescription(taskDescriptionConfig: TaskDescriptionConfig) {
    configureTaskDescription(taskDescriptionConfig.title, taskDescriptionConfig.icon, taskDescriptionConfig.color)
}
