---
id: rate-app
title: Rate App
description: Set of utilities to invite the user to rate the app.
---

#### <a href="/reference/-android%20-utils/com.jeovanimartinez.androidutils.reviews/-rate-app/index.html" target="_blank"><b>[ Reference ]</b></a>

## Description

Set of utilities to invite the user to rate the app.

> [**Rate In App**](#rate-in-app) This utility invites the user to rate the app with Google Play In-App Reviews API, based on certain conditions of use of the app.

> [**Rate In Google Play**](#rate-in-google-play) This utility directs the user to the app details on Google Play to rate it there.

---
---

## Rate In App

### Description

Utility to show a flow that invites the user to rate the app, based on certain conditions on the use of the app (how many days do it have installed, 
how many times it has been launched, etc.). The [Google Play In-App Review API](https://developer.android.com/guide/playcore/in-app-review) 
is used for this utility.

<p align="center"><img src={require('@site/docs/img/rate-app/rate-app-img1.png').default} alt="" /></p>

---

### Usage

Either in the `onCreate()` of the application class or in the main activity you have to set the configuration values and initialize the utility by passing a context. 
It is very important to do it only once in the app, since at that moment the times the user has launched the app are counted.

> #### <a href="/reference/-android%20-utils/com.jeovanimartinez.androidutils.reviews/-rate-app/index.html" target="_blank"><b>[ General Reference ]</b></a>
> #### <a href="/reference/-android%20-utils/com.jeovanimartinez.androidutils.reviews/-rate-app/min-install-elapsed-days.html" target="_blank"><b>&nbsp;&nbsp;> minInstallElapsedDays</b></a>
> #### <a href="/reference/-android%20-utils/com.jeovanimartinez.androidutils.reviews/-rate-app/min-install-launch-times.html" target="_blank"><b>&nbsp;&nbsp;> minInstallLaunchTimes</b></a>
> #### <a href="/reference/-android%20-utils/com.jeovanimartinez.androidutils.reviews/-rate-app/min-remind-elapsed-days.html" target="_blank"><b>&nbsp;&nbsp;> minRemindElapsedDays</b></a>
> #### <a href="/reference/-android%20-utils/com.jeovanimartinez.androidutils.reviews/-rate-app/min-remind-launch-times.html" target="_blank"><b>&nbsp;&nbsp;> minRemindLaunchTimes</b></a>
> #### <a href="/reference/-android%20-utils/com.jeovanimartinez.androidutils.reviews/-rate-app/show-at-event.html" target="_blank"><b>&nbsp;&nbsp;> showAtEvent</b></a>

```kotlin
RateApp.apply {
    minInstallElapsedDays = 10
    minInstallLaunchTimes = 10
    minRemindElapsedDays = 2
    minRemindLaunchTimes = 4
    showAtEvent = 2
}.init(context)
```

Since the utility has been configured, call the next method at the time you want to show the flow to rate the app, the flow will be launched only if the 
all conditions specified in the configuration are met.
```kotlin
RateApp.checkAndShow(activity)
```

:::tip
The utility provides a detailed execution flow in the Logcat that you can consult to see the validation of the conditions and perform tests.

<p align="center"><img src={require('@site/docs/img/rate-app/rate-app-img2.png').default} alt="" /></p>
:::

:::tip
You can modify the date of your device to be able to run simulate the course of days since the app was installed and perform tests.
:::

---

### Considerations

#### In-App Review API

- It is highly recommended to read the official [In-App Review API documentation](https://developer.android.com/guide/playcore/in-app-review) to 
understand how the API behaves.
- The Google Play In-App Review API has a [limited quota](https://developer.android.com/guide/playcore/in-app-review#quotas) to be able to show the 
flow to rate the app, so on certain occasions, all the conditions may be met, but the flow will be not launched.
- The app doesn't have to be published to test, but the app must be available at least in the internal testing track.
- After deleting a revision, it is recommended to clear data and delete the cache of Google Play Store app to be able to carry out the tests and that the flow shown again.

---
---

## Rate In Google Play

### Description

Utility to direct the user to app details on Google Play Store, usually used to invite the user to rate the app.

<p align="center"><img src={require('@site/docs/img/rate-app/rate-app-img3.png').default} alt="" /></p>

:::note
If the Google Play app is not available on the device, the user is directed to app details on the Google Play website on the system web browser.
:::

### Usage

```kotlin
RateApp.goToRateInGooglePlay(activity)
```
