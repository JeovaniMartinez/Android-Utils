---
id: getting-started
title: Getting Started
sidebar_label: Getting Started
slug: /
description: How to include and configure the library in your project.
---

Apply the following configuration in your Android project to start using the Android Utils library.

### Gradle Setup

**1.** Add the following line in the `settings.gradle` file.

```gradle {6}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

_If your project uses an old version of Gradle, the line must be added in the Gradle file `build.gradle` at the project level._

```gradle {4}
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

**2.** Add the following line in the Gradle file `build.gradle` at the app level.

```gradle {3}
dependencies {
    ...
    implementation 'com.github.JeovaniMartinez:Android-Utils:v1.0.0'
}
```

:::tip
Depending on your needs, you can use any other version of the [library releases,](https://github.com/JeovaniMartinez/Android-Utils/releases) the 
short commit hash, 'master-SNAPSHOT' or 'develop-SNAPSHOT'.
:::

---

### Style

The library can show some views, which follow the style of the app theme and have support for the light and dark theme, 
for these views to be shown with the app style it is necessary to declare the following style in the resources, which must 
inherit from the main app theme. The style must be added in the file `themes.xml` or `styles.xml`.

```xml
<!-- Android Utils library theme, used to preserve the app style. -->
<style name="AndroidUtilsTheme" parent="AppTheme" />
```

:::info
The name of the style resource must be exactly  `AndroidUtilsTheme`
:::
