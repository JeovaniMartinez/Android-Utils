---
id: system-web-browser
title: System Web Browser
---

#### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.web/-system-web-browser/index.html" target="_blank"><b>[ Reference ]</b></a>

## Description

Utility to interact with the system web browser.

---

## Utilities

### - openUrl()

> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.web/-system-web-browser/open-url.html" target="_blank"><b>[ Reference ]</b></a>

Open the system web browser at the specified URL.

#### Usage

```kotlin
SystemWebBrowser.openUrl(context, "https://jedemm.com")
```

If Firebase Analytics is enabled, you can pass an argument indicating the reason why the browser opens, this argument is registered as a parameter in an 
event in Firebase Analytics.

```kotlin
SystemWebBrowser.openUrl(context, "https://jedemm.com", "jedemm_website")
```
