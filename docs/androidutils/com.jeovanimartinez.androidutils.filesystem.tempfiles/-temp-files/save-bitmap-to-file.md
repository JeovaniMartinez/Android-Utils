[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.filesystem.tempfiles](../index.md) / [TempFiles](index.md) / [saveBitmapToFile](./save-bitmap-to-file.md)

# saveBitmapToFile

`fun saveBitmapToFile(context: Context, bitmap: Bitmap): `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)

Guarda la imagen [bitmap](save-bitmap-to-file.md#com.jeovanimartinez.androidutils.filesystem.tempfiles.TempFiles$saveBitmapToFile(android.content.Context, android.graphics.Bitmap)/bitmap) en un archivo temporal en la carpeta TEMP_FILES_DIR, el nombre
del archivo es un UUID generado y en formato PNG, al final regresa el archivo creado. Para imágenes pequeñas
el proceso es rápido y puede llamarse de manera síncrona, para imágenes grandes se recomienda llamar de manera
asíncrona.
IMPORTANTE: Llamar siempre dentro de un bloque try catch.

