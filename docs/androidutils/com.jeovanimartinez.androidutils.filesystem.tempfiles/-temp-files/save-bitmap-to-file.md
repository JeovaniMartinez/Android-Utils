//[androidutils](../../index.md)/[com.jeovanimartinez.androidutils.filesystem.tempfiles](../index.md)/[TempFiles](index.md)/[saveBitmapToFile](save-bitmap-to-file.md)



# saveBitmapToFile  
[androidJvm]  
Content  
fun [saveBitmapToFile](save-bitmap-to-file.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), bitmap: [Bitmap](https://developer.android.com/reference/kotlin/android/graphics/Bitmap.html)): [File](https://developer.android.com/reference/kotlin/java/io/File.html)  
More info  


Guarda la imagen bitmap en un archivo temporal en la carpeta TEMP_FILES_DIR, el nombre del archivo es un UUID generado y en formato PNG, al final regresa el archivo creado. Para imágenes pequeñas el proceso es rápido y puede llamarse de manera síncrona, para imágenes grandes se recomienda llamar de manera asíncrona. IMPORTANTE: Llamar siempre dentro de un bloque try catch.

  



