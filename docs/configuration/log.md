---
id: log
title: Log
description: How to enable and disable the library's debug log.
---

export const Error = ({children}) => ( <span style={{
backgroundColor: '#ff6b68',
borderRadius: '6px',
color: '#fff',
padding: '0.2rem 0.8rem',
}}>{children}</span> );

export const Warring = ({children}) => ( <span style={{
backgroundColor: '#dbb52b',
borderRadius: '6px',
color: '#fff',
padding: '0.2rem 0.8rem',
}}>{children}</span> );

export const General = ({children}) => ( <span style={{
backgroundColor: '#3fa4f8',
borderRadius: '6px',
color: '#fff',
padding: '0.2rem 0.8rem',
}}>{children}</span> );

## Description

The library uses different logging levels to report errors, warnings, useful messages, and the flow of execution of the utilities.

:::tip
You can use **[these instructions](https://gist.github.com/JeovaniMartinez/386fa7cd5a9c1afdc12b64c9706fd5fe)** to show the logcat messages colored.
:::

:::note
If you have configured `minifyEnabled true` in android > buildTypes > release on your app Gradle file, all log messages emitted by the library will be 
removed from the APK or App Bundle you generate for production, as they are not considered useful or necessary for this environment.
:::

---

## Logging Levels

### <Error>Error</Error>

It uses `Log.e()`, it indicates that something has not worked as expected, depending on the error, it can only 
appear the message or the exception details in the logcat, or it can cause the application to stop.

### <Warring>Warning</Warring>

It uses `Log.w()`, it indicates the suspicion that something shady is happening, it may not be exactly an error, 
but it may also be unexpected behavior.

### <General>General</General>

It uses `Log.v()`, it's used for all other logcat messages, such as useful messages about the library utilities, and to 
detail the execution flow of the utilities.

The general log can enable and disable as follows:

```kotlin
Base.logEnable = true // or false
```

The recommended configuration for the general log is the following, use it the `onCreate()` of the application class or in the main activity.

```kotlin
Base.logEnable = BuildConfig.DEBUG
```
:::note
<a href="/reference/-android%20-utils/com.jeovanimartinez.androidutils/-base/index.html" target="_blank"><b>Base</b></a> is the superclass of most utilities.
:::
