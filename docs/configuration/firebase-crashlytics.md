---
id: firebase-crashlytics
title: Firebase Crashlytics
---

:::note
Recoverable errors registry into Firebase Crashlytics is **optional** and is disabled by default.
:::

## Configuration

:::important
If you want to enable errors registry, your project must have Firebase Crashlytics configured.<br/>
[Guide](https://firebase.google.com/docs/crashlytics/get-started?platform=android)
:::

The library can register some recoverable error streams in Firebase Crashlytics, for this it is necessary to assign the crashlytics instance to the 
library, this must be done in the `onCreate()` of the singleton or main activity.

To enable recoverable errors register:
```kotlin
Base.firebaseCrashlyticsInstance = FirebaseCrashlytics.getInstance()
```

:::note
<a href="../reference/androidutils/com.jeovanimartinez.androidutils/-base/index.html" target="_blank"><b>Base</b></a> is the superclass of  most utilities.
:::
