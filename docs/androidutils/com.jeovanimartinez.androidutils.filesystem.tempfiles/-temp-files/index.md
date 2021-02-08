[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.filesystem.tempfiles](../index.md) / [TempFiles](./index.md)

# TempFiles

`object TempFiles : `[`Base`](../../com.jeovanimartinez.androidutils/-base/index.md)`<`[`TempFiles`](./index.md)`>`

Utilidad para trabajar con archivos temporales

### Properties

| Name | Summary |
|---|---|
| [LOG_TAG](-l-o-g_-t-a-g.md) | Etiqueta par el log`val LOG_TAG: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [clearTempFilesFolder](clear-temp-files-folder.md) | Elimina todos los archivos de la carpeta de archivos temporales (TEMP_FILES_DIR). Se recomienda invocar esta función al iniciar la app, para no acumular muchos archivos, o bien invocar cuando sea requerido limpiar la carpeta. El proceso se ejecuta en una corrutina con para no afectar el flujo de la app.`fun clearTempFilesFolder(context: Context): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [saveBitmapToFile](save-bitmap-to-file.md) | Guarda la imagen [bitmap](save-bitmap-to-file.md#com.jeovanimartinez.androidutils.filesystem.tempfiles.TempFiles$saveBitmapToFile(android.content.Context, android.graphics.Bitmap)/bitmap) en un archivo temporal en la carpeta TEMP_FILES_DIR, el nombre del archivo es un UUID generado y en formato PNG, al final regresa el archivo creado. Para imágenes pequeñas el proceso es rápido y puede llamarse de manera síncrona, para imágenes grandes se recomienda llamar de manera asíncrona. IMPORTANTE: Llamar siempre dentro de un bloque try catch.`fun saveBitmapToFile(context: Context, bitmap: Bitmap): `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
