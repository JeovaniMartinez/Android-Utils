//[androidutils](../../index.md)/[com.jeovanimartinez.androidutils.filesystem.tempfiles](../index.md)/[TempFiles](index.md)



# TempFiles  
 [androidJvm] object [TempFiles](index.md) : [Base](../../com.jeovanimartinez.androidutils/-base/index.md)<[TempFiles](index.md)> 

Utilidad para trabajar con archivos temporales

   


## Functions  
  
|  Name|  Summary| 
|---|---|
| <a name="com.jeovanimartinez.androidutils.filesystem.tempfiles/TempFiles/clearTempFilesFolder/#android.content.Context/PointingToDeclaration/"></a>[clearTempFilesFolder](clear-temp-files-folder.md)| <a name="com.jeovanimartinez.androidutils.filesystem.tempfiles/TempFiles/clearTempFilesFolder/#android.content.Context/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [clearTempFilesFolder](clear-temp-files-folder.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))  <br>More info  <br>Elimina todos los archivos de la carpeta de archivos temporales (TEMP_FILES_DIR).  <br><br><br>
| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[equals](../../com.jeovanimartinez.androidutils.web/-system-web-browser/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F-1006092240)| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open operator fun [equals](../../com.jeovanimartinez.androidutils.web/-system-web-browser/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F-1006092240)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[hashCode](../../com.jeovanimartinez.androidutils.web/-system-web-browser/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F-1006092240)| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open fun [hashCode](../../com.jeovanimartinez.androidutils.web/-system-web-browser/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F-1006092240)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| <a name="com.jeovanimartinez.androidutils.filesystem.tempfiles/TempFiles/saveBitmapToFile/#android.content.Context#android.graphics.Bitmap/PointingToDeclaration/"></a>[saveBitmapToFile](save-bitmap-to-file.md)| <a name="com.jeovanimartinez.androidutils.filesystem.tempfiles/TempFiles/saveBitmapToFile/#android.content.Context#android.graphics.Bitmap/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [saveBitmapToFile](save-bitmap-to-file.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), bitmap: [Bitmap](https://developer.android.com/reference/kotlin/android/graphics/Bitmap.html)): [File](https://developer.android.com/reference/kotlin/java/io/File.html)  <br>More info  <br>Guarda la imagen bitmap en un archivo temporal en la carpeta TEMP_FILES_DIR, el nombre del archivo es un UUID generado y en formato PNG, al final regresa el archivo creado.  <br><br><br>
| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[toString](../../com.jeovanimartinez.androidutils.web/-system-web-browser/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F-1006092240)| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open fun [toString](../../com.jeovanimartinez.androidutils.web/-system-web-browser/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F-1006092240)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| <a name="com.jeovanimartinez.androidutils.filesystem.tempfiles/TempFiles/firebaseAnalyticsEnabled/#/PointingToDeclaration/"></a>[firebaseAnalyticsEnabled](index.md#%5Bcom.jeovanimartinez.androidutils.filesystem.tempfiles%2FTempFiles%2FfirebaseAnalyticsEnabled%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-1006092240)| <a name="com.jeovanimartinez.androidutils.filesystem.tempfiles/TempFiles/firebaseAnalyticsEnabled/#/PointingToDeclaration/"></a> [androidJvm] var [firebaseAnalyticsEnabled](index.md#%5Bcom.jeovanimartinez.androidutils.filesystem.tempfiles%2FTempFiles%2FfirebaseAnalyticsEnabled%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-1006092240): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)Determina si el registro de eventos en Firebase Analytics esta habilitado para la instancia de la clase, para el registro de eventos, la propiedad estática del companion object firebaseAnalyticsInstance también debe estar asignada, si esa propiedad es null, no se van a registrar eventos, ya que es requerida   <br>

