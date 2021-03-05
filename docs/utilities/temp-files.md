---
id: temp-files
title: Temp Files
---

#### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.filesystem.tempfiles/-temp-files/index.html" target="_blank"><b>[ Reference ]</b></a>

## Description

Utility to easily manipulate the app temporary files. Temporary files are created in a private folder of the app named `androidutils_tempfiles`

---

## Utilities

### - clearTempFilesFolder()

> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.filesystem.tempfiles/-temp-files/clear-temp-files-folder.html" target="_blank"><b>[ Reference ]</b></a>

Delete all files created with this utility (the files in the temporary folder), it is recommended to call in the start of the app or where you need to 
clean the contents of the folder. The function runs asynchronously so as not to affect the app flow and can be called from anywhere.

#### Usage

```kotlin
TempFiles.clearTempFilesFolder(context)
```

---

### - saveBitmapToFile()

> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.filesystem.tempfiles/-temp-files/save-bitmap-to-file.html" target="_blank"><b>[ Reference ]</b></a>

Save a bitmap object into PNG image file.

#### Usage

```kotlin
TempFiles.saveBitmapToFile(context, bitmap, "test")
```
