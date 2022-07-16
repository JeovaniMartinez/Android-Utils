---
id: firebase-crashlytics
title: Firebase Crashlytics
description: Optional crash reports in Firebase Crashlytics.
---

## Description

The library can report some exceptions to Firebase Crashlytics so that bugs can be detected and fixed.

:::note
Reports are sent to the Firebase account of the project using the library, so it's recommended that you 
enable Firebase Crashlytics if you plan to contribute to the development of the library.
:::

---

## Configuration

:::important
If you want to enable crash reports of the library, your project must have Firebase Crashlytics configured.<br/>
**[Guide.](https://firebase.google.com/docs/crashlytics/get-started?platform=android)**
:::

To enable the crash reports, put the following line of code in the `onCreate()` of the application class or in 
the main activity.

```kotlin
Base.firebaseCrashlyticsInstance = FirebaseCrashlytics.getInstance()
```
