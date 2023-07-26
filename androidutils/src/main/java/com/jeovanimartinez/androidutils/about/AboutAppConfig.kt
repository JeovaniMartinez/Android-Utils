package com.jeovanimartinez.androidutils.about

import androidx.annotation.ColorInt
import com.jeovanimartinez.androidutils.activity.config.TaskDescriptionConfig
import com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes

/**
 * Configuration for the about activity.
 * @param backgroundColor Background color for the activity.
 * @param textColor Color for the texts.
 * @param iconsColor Color for the icons.
 * @param appIcon App icon or logo.
 * @param appName App name.
 * @param appVersionName App version.
 * @param authorName App author name.
 * @param authorLink URL that opens when clicking on the author's name, null to not open any link.
 * @param companyLogo Developer company logo.
 * @param companyName Developer company name.
 * @param companyLink URL that opens when clicking on the company logo, null to not open any link.
 * @param termsAndPrivacyPolicyLink URL of the terms of use and privacy policy, leave null if you do not want to show them.
 * @param showOpenSourceLicenses Determines whether a button is shown to view the open source licenses of the app. If true, these https://developers.google.com/android/guides/opensource
 *        instructions must be followed in order to can shown open source licenses.
 * @param taskDescriptionConfig Object with the configuration for the activity TaskDescription, leave null if it is not required.
 * */
data class AboutAppConfig(
    @ColorInt val backgroundColor: Int,
    @ColorInt val textColor: Int,
    @ColorInt val iconsColor: Int,
    @DrawableOrDrawableRes val appIcon: Any?,
    @StringOrStringRes val appName: Any,
    @StringOrStringRes val appVersionName: Any,
    @StringOrStringRes val authorName: Any,
    @StringOrStringRes val authorLink: Any?,
    @DrawableOrDrawableRes val companyLogo: Any?,
    @StringOrStringRes val companyName: Any,
    @StringOrStringRes val companyLink: Any?,
    @StringOrStringRes val termsAndPrivacyPolicyLink: Any?,
    val showOpenSourceLicenses: Boolean,
    val taskDescriptionConfig: TaskDescriptionConfig? = null,
)
