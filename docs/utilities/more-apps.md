---
id: more-apps
title: More Apps
---

#### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.moreapps/-more-apps-g-play/index.html" target="_blank"><b>[ Reference ]</b></a>

## Description

Utility to direct the user to the app developer page on Google Play, usually used to invite the user to install the developer apps.

![img](../img/more-apps/more-apps-img1.png)

---

## Usage

```kotlin
MoreAppsGPlay.apply { developerId = "GitHub" }.showAppList(activity)
```

```kotlin
MoreAppsGPlay.showAppList(activity)
```
