package com.jeovanimartinez.androidutils.about

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes

/**
 * Configuration for about activity.
 * @param backgroundColor Background color for the activity, if null, the default value is used, which is the background color of the theme.
 * @param iconsColor Color for icons, if null, the default value is used, which is the color defined in the theme.
 * @param appIcon App icon or logo.
 * @param appName App name.
 * @param appVersionName App version.
 * @param authorName App author name.
 * @param authorLink URL that opens when clicking on the author's name, null to not open any link.
 * @param companyLogo Developer company logo.
 * @param companyName Developer company name.
 * @param companyLink URL that opens when clicking on the company logo, null to not open any link.
 * @param termsAndPrivacyPolicyLink URL of the terms of use and privacy policy, leave null if you do not want to show them.
 * @param termsAndPrivacyPolicyTextColor Color for the text of the terms of use and privacy policy, if it is null, the default value is used, which is the color defined in the theme.
 * @param showOpenSourceLicenses Determines whether a button is shown to view the app open source licenses. If true, these https://developers.google.com/android/guides/opensource
 *        instructions must be followed in order to can shown open source licenses.
 * PENDING CHANGE TASK DESCRIPTION PROPERTIES TO OBJECT
 * */
data class AboutAppConfig(
    @ColorInt val backgroundColor: Int? = null,
    @ColorInt val iconsColor: Int? = null,
    @DrawableOrDrawableRes val appIcon: Any?,
    @StringOrStringRes val appName: Any,
    @StringOrStringRes val appVersionName: Any,
    @StringOrStringRes val authorName: Any,
    @StringOrStringRes val authorLink: Any?,
    @DrawableOrDrawableRes val companyLogo: Any?,
    @StringOrStringRes val companyName: Any,
    @StringOrStringRes val companyLink: Any?,
    @StringOrStringRes val termsAndPrivacyPolicyLink: Any?,
    @ColorInt val termsAndPrivacyPolicyTextColor: Int? = null,
    val showOpenSourceLicenses: Boolean,
    @StringOrStringRes val taskDescriptionTitle: Any? = null,
    @DrawableRes val taskDescriptionIcon: Int? = null,
    @ColorInt val taskDescriptionColor: Int? = null
)
