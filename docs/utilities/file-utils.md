---
id: file-utils
title: File Utils
---

#### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.filesystem/-file-utils/index.html" target="_blank"><b>[ Reference ]</b></a>

## Description

Set of utilities for working with file system.

---

## Utilities

### - saveBitmapToFile

> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.filesystem/-file-utils/save-bitmap-to-file.html" target="_blank"><b>[ Reference ]</b></a>

Save a bitmap object into image file.

#### Usage

```kotlin {8}
// Create a bitmap object for test the utility and draw a color and text on it.
val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
val canvas = Canvas(bitmap)
canvas.drawColor(Color.DKGRAY)
canvas.drawText("Android Utils", 35f, 60f, Paint().apply { color = Color.WHITE; textSize = 50f; isAntiAlias = true })

// Use FileUtils to save bitmap into image file.
FileUtils.saveBitmapToFile(context, bitmap, "test", context.filesDir.absolutePath, Bitmap.CompressFormat.PNG, 100)
```

<br/>

---
---

## Temporary Files

#### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.filesystem/-temp-files/index.html" target="_blank"><b>[ Reference ]</b></a>

Utility to work with temporary files.

> Temporary Files is a sub utility of File Utils.

In many cases we create files for temporary use, only required for a specific case and that we do not want to keep since they only use memory storage.
This utility helps us to simplify this process. When files are created with File Utils, the `path` parameter can be passed as `null`, in this case, the 
created file will be saved in a private temporary folder of the app. These files remain in this folder so that they can be manipulated with our app.

When we need to clean the temporary files folder we can do it by:

> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.filesystem/-temp-files/clear-temp-files-folder.html" target="_blank"><b>[ Reference ]</b></a>

```kotlin
TempFiles.clearTempFilesFolder(context)
```
