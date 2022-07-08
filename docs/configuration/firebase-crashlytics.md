---
id: firebase-crashlytics
title: Firebase Crashlytics
description: Optional recoverable error's registry into Firebase Crashlytics.
---

:::note
Recoverable error's registry into Firebase Crashlytics is **optional** and is disabled by default.
:::

:::warning
If you enable error logging, the logs will be visible on your firebase account, so it is only recommended that you enable it if you plan to contribute 
to the development of the library.
:::

## Configuration

:::important
If you want to enable errors registry, your project must have Firebase Crashlytics configured.<br/>
[Guide](https://firebase.google.com/docs/crashlytics/get-started?platform=android)
:::

To enable recoverable errors register, put the following line of code in the `onCreate()` of the singleton or main activity:

```kotlin
Base.firebaseCrashlyticsInstance = FirebaseCrashlytics.getInstance()
```
