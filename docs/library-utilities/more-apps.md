---
id: more-apps
title: More Apps
---

#### <a href="../reference/-android%20-utils/com.jeovanimartinez.androidutils.moreapps/-more-apps/index.html" target="_blank"><b>[ Reference ]</b></a>

## Description

Utility to invite the user to install the developer's apps.

---

## Utilities

### - showAppListInGooglePlay

> #### <a href="../reference/-android%20-utils/com.jeovanimartinez.androidutils.moreapps/-more-apps/show-app-list-in-google-play.html" target="_blank"><b>[ Reference ]</b></a>

Direct the user to the app developer page on Google Play, usually used to invite the user to install the developer apps.

<p align="center"><img src={require('@site/docs/img/more-apps/more-apps-img1.png').default} alt="" /></p>

#### Usage

1.- Set the developer id.

```kotlin
MoreApps.apply { developerId = "Jedemm+Technologies" }
```

2.- Go to Google Play to show the list of apps from the developer.

```kotlin
MoreApps.showAppListInGooglePlay(activity)
```
