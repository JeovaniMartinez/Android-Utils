---
id: about-app
title: About App
---

## Description

Utility to show an about app activity, which includes the information of the app, the author, the company, the copyright, as well as the open source 
licenses, the license terms and the privacy policy. The activity is optimized to adjust elements in portrait and landscape screen modes.

<p align="center"><img src={require('@site/docs/img/about-app/about-app-img1.png').default} alt="" /></p>

---

## Usage

First you need to create a configuration object.

> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.about/-about-app-config/index.html" target="_blank"><b>[ Configuration Parameters  ]</b></a>

```kotlin
val aboutAppConfig = AboutAppConfig(
    backgroundColor = getColorCompat(R.color.colorBackground),
    iconsColor = getColorCompat(R.color.colorIcon),
    appIcon = R.drawable.library_logo,
    appName = R.string.about_app_app_name,
    appVersionName = BuildConfig.VERSION_NAME,
    authorName = R.string.about_app_author_name,
    authorLink = R.string.about_app_author_link,
    companyLogo = R.drawable.logo_jedemm_com,
    companyName = R.string.about_app_company_name,
    companyLink = R.string.about_app_company_link,
    termsAndPrivacyPolicyLink = R.string.about_app_terms_and_policy_link,
    termsAndPrivacyPolicyTextColor = getColorCompat(R.color.colorTermsAndPrivacyPolicyText),
    showOpenSourceLicenses = true,
    TaskDescriptionConfig(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.colorBackground))
)
```

Then to display the screen you have to call the following function passing the configuration object and an activity as arguments.

#### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.about/-about-app/index.html" target="_blank"><b>[ Reference ]</b></a>

```kotlin
AboutApp.show(activity, aboutAppConfig)
```

---

## Considerations

### Open Source Licenses

<p align="center"><img src={require('@site/docs/img/about-app/about-app-img2.png').default} alt="" /></p>

If you want to display button to show open source licenses activity, the configuration parameter `showOpenSourceLicenses` must be `tru`e and you need 
to apply the following configuration in your project.

Add the following line in Gradle file at project level.

```gradle {4}
buildscript {
    dependencies {
        ...
        classpath "com.google.android.gms:oss-licenses-plugin:0.10.2"
    }
}
```

Add the following line in Gradle file at app level.
```gradle {3}
plugins {
    ...
    id 'com.google.android.gms.oss-licenses-plugin'
}
```

:::caution
If you do not apply the indicated configuration, the activity that shows open source licenses will not display any content.
:::

---

### Terms & Privacy Policy

<p align="center"><img src={require('@site/docs/img/about-app/about-app-img3.png').default} alt="" /></p>

The activity show the terms and policy in a `WebView`, which are loaded from a URL `termsAndPrivacyPolicyLink` so that the most up-to-date version can 
always be shown. In order to display the web page with the style of the theme that the app uses, two parameters are sent to the URL: `background-color` 
and `text-color` to customize the background color of the page and the text color respectively.

The following template for the page is the one used in the example, and it already takes care of processing the parameters and adjusting the view:

> #### <a href="https://github.com/JeovaniMartinez/Android-Utils/tree/master/resources/terms-and-privacy-policy" target="_blank"><b>[ Template ]</b></a>
> #### <a href="https://jedemm.com/android-utils/terms-and-policy/license.html?background-color=212121&text-color=CFCFCF&lang=en" target="_blank"><b>[ Demo ]</b></a>

:::tip
You can change the URL parameters `background-color=212121&text-color=CFCFCF` to customize the page style.
:::

:::note
Additionally, when generating the url for the terms & policy, the library also adds the lang parameter with the user's current language code.

Parameter example: `lang=en`

You can use that parameter to display the terms in multiple languages, or if you have different urls for each language, put each url in the 
corresponding strings.xml file and ignore the parameter.
:::
