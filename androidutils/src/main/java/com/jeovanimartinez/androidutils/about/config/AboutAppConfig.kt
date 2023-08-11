package com.jeovanimartinez.androidutils.about.config

import com.jeovanimartinez.androidutils.activity.config.TaskDescriptionConfig
import com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes
import com.jeovanimartinez.androidutils.annotations.StringOrStringRes

/**
 * Configuration data for the about app activity.
 * @param appLogo Logo or app icon.
 * @param appName App name.
 * @param appVersion App version name.
 * @param creditsText App credits. For an independent app or one developed by an individual, could be the developer's name.
 *        For a company, it could be a short text like: My Company Dev-Team. Always use a short text that takes up a
 *        maximum of one line.
 * @param creditsUrl It is optional and only takes effect if [creditsText] is not null, determining which URL will be opened in the
 *        system's web browser when clicking on [creditsText]. Set to null to not open any URL.
 * @param authorLogo The app's author's logo, it can be the company's logo or any logo that represents the app's author.
 * @param authorUrl It is optional, determining which URL will be opened in the system's web browser when clicking on [authorLogo].
 *        Set to null to not open any URL.
 * @param copyrightHolderName Name of the copyright holder to be used in the copyright statement.
 * @param termsAndPrivacyPolicyUrl The URL for the app's license terms and privacy policy, which will be displayed within the
 *        about app activity in a WebView. Set to null to not show this section or the corresponding button.
 * @param showOpenSourceLicenses Determines whether to show a button that launch an activity showing the open-source licenses
 *        used by the app. If set to true, it is necessary to follow these instructions to display the licenses
 *        correctly: https://developers.google.com/android/guides/opensource
 * @param helpCenterUrl URL for the app's help center, which will be opened in the system's web browser. Set to null to not show
 *        this option.
 * @param crashReportEmail Email for app crash report. Set to null to not show this option.
 * @param contactEmail Email for contact. Set to null to not show this option.
 * @param feedbackEmail Email for sending feedback about the app. Set to null to not show this option.
 * @param style Sets the configuration for the about app activity's style. If set to null, the values from the resource files will
 *        be used. If assigned, the specified values will be used, and the values from the resource files will be ignored.
 * @param taskDescriptionConfig Object with the configuration for the activity TaskDescription, leave null if it is not required.
 * */
data class AboutAppConfig(
    @DrawableOrDrawableRes val appLogo: Any,
    @StringOrStringRes val appName: Any,
    @StringOrStringRes val appVersion: Any,
    @StringOrStringRes val creditsText: Any,
    @StringOrStringRes val creditsUrl: Any?,
    @DrawableOrDrawableRes val authorLogo: Any,
    @StringOrStringRes val authorUrl: Any?,
    @StringOrStringRes val copyrightHolderName: Any,
    @StringOrStringRes val termsAndPrivacyPolicyUrl: Any?,
    val showOpenSourceLicenses: Boolean,
    @StringOrStringRes val helpCenterUrl: Any?,
    @StringOrStringRes val crashReportEmail: Any?,
    @StringOrStringRes val contactEmail: Any?,
    @StringOrStringRes val feedbackEmail: Any?,
    val style: AboutAppStyle? = null,
    val taskDescriptionConfig: TaskDescriptionConfig? = null,
)
