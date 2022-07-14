---
id: log
title: Log
description: How to enable and disable the library's debug log.
---

## Description

The debug log describes in detail the execution flow of the library utilities.

## Configuration

The debug log can be globally enabled or disabled, and affects all utilities in the library. It is recommended to use the following configuration to
enable it in development and disable it in production, it is only necessary to adjust it once within the app, either in the `onCreate()` of the 
application class or on the main activity.
```kotlin
Base.logEnable = BuildConfig.DEBUG
```
:::note
<a href="/reference/-android%20-utils/com.jeovanimartinez.androidutils/-base/index.html" target="_blank"><b>Base</b></a> is the superclass of  most utilities.
:::
