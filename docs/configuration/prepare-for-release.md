---
id: prepare-for-release
title: Prepare For Production Release
sidebar_label: Prepare For Release
description: Notes and considerations to prepare your app release for production.
---

## Optimize Your App

This library has a variety of utilities for different purposes, and you will probably only use some utilities, so it is recommended 
that you read
**[this guide](https://developer.android.com/studio/build/shrink-code)**
to configure your app release to remove unused code and resources.

Once you set `minifyEnabled true` in android > buildTypes > release on your app Gradle file, all unused code from the library will be removed 
from the APK or App Bundle you generate for production.

:::important
It is very important that you read and understand the
**[shrink, obfuscate, and optimize your app](https://developer.android.com/studio/build/shrink-code)** guide
before enabling `minifyEnabled true` option.
:::

---

## Remove Log

The log messages emitted by the library are useful when developing and debugging the app, but are often unnecessary in the production release.
The library has
[ProGuard rules](https://github.com/JeovaniMartinez/Android-Utils/blob/master/androidutils/consumer-rules.pro)
configured to delete automatically all log messages emitted by the library in the APK or App Bundle you generate for production.
Note that for these rules to apply, you must have configured `minifyEnabled true` in your app Gradle file.
