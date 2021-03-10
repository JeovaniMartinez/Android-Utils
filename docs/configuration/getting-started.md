---
id: getting-started
title: Getting Started
sidebar_label: Getting Started
slug: /
---

Apply the next configuration in your Android project to start to use the Android Utils library.

### Gradle Setup

Add the following line in Gradle file at project level.

```gradle {4}
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the following line in Gradle file at app level. Replace _VERSION_ to the [latest release version.](https://github.com/JeovaniMartinez/Android-Utils/releases)
```gradle {3}
dependencies {
    ...
    implementation 'com.github.JeovaniMartinez:Android-Utils:VERSION'
}
```

---

### Style

The library can show some views, which follow the style of the app theme and have support for the light and dark theme,
for that these views are shown with the app style it is necessary to declare the following style in the resources, which 
must inherit from the main app theme.

```xml
<!-- Android Utils library theme, used to preserve the app style. -->
<style name="AndroidUtilsTheme" parent="AppTheme" />
```