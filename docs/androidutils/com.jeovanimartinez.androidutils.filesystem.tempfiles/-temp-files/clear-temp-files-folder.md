[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.filesystem.tempfiles](../index.md) / [TempFiles](index.md) / [clearTempFilesFolder](./clear-temp-files-folder.md)

# clearTempFilesFolder

`fun clearTempFilesFolder(context: Context): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Elimina todos los archivos de la carpeta de archivos temporales (TEMP_FILES_DIR).
Se recomienda invocar esta función al iniciar la app, para no acumular muchos archivos, o bien invocar cuando
sea requerido limpiar la carpeta. El proceso se ejecuta en una corrutina con para no afectar el flujo de la app.

