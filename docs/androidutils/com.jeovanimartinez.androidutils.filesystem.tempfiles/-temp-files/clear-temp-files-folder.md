//[androidutils](../../index.md)/[com.jeovanimartinez.androidutils.filesystem.tempfiles](../index.md)/[TempFiles](index.md)/[clearTempFilesFolder](clear-temp-files-folder.md)



# clearTempFilesFolder  
[androidJvm]  
Content  
fun [clearTempFilesFolder](clear-temp-files-folder.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))  
More info  


Elimina todos los archivos de la carpeta de archivos temporales (TEMP_FILES_DIR). Se recomienda invocar esta función al iniciar la app, para no acumular muchos archivos, o bien invocar cuando sea requerido limpiar la carpeta. El proceso se ejecuta en una corrutina con para no afectar el flujo de la app.

  



