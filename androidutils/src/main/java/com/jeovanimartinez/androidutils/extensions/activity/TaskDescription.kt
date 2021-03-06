@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.activity

import android.app.Activity
import android.app.ActivityManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.jeovanimartinez.androidutils.activity.config.TaskDescriptionConfig
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes
import com.jeovanimartinez.androidutils.extensions.context.typeAsString

/**
 * Extensions to configure activities TaskDescription
 * */

/**
 * Configure the activity TaskDescription, it works from Android 5 onwards and adjusts the config automatically
 * for the different versions of Android.
 *
 * @param title Title for the task description, string or resource ID.
 * @param icon Icon to show (resource id), it must be a PNG image, if the resource id is SVG, it will not be applied and the app icon will be shown.
 * @param color Color-int for the background.
 * */
fun Activity.configureTaskDescription(@StringOrStringRes title: Any, @DrawableRes icon: Int, @ColorInt color: Int) {

    val finalTitle = typeAsString(title)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

        // For Android Pie (9.0) API 28 and later versions
        val taskDescription = ActivityManager.TaskDescription(finalTitle, icon, color)
        this.setTaskDescription(taskDescription)

    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        // From Android 5.0 to Android 8.1
        @Suppress("DEPRECATION")
        val taskDescription = ActivityManager.TaskDescription(finalTitle, BitmapFactory.decodeResource(resources, icon), color)
        this.setTaskDescription(taskDescription)

    }

    // No have support for versions prior to Android 5

}

/**
 * Configure the activity TaskDescription, it works from Android 5 onwards and adjusts the config automatically
 * for the different versions of Android.
 *
 * @param taskDescriptionConfig Object with the configuration for the activity TaskDescription.
 * */
fun Activity.configureTaskDescription(taskDescriptionConfig: TaskDescriptionConfig) {
    configureTaskDescription(taskDescriptionConfig.title, taskDescriptionConfig.icon, taskDescriptionConfig.color)
}
