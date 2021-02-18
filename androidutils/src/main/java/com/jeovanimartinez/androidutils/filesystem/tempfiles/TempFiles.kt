package com.jeovanimartinez.androidutils.filesystem.tempfiles

import android.content.Context
import android.graphics.Bitmap
import com.jeovanimartinez.androidutils.Base
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * Utilidad para trabajar con archivos temporales
 * */
object TempFiles : Base<TempFiles>() {

    private const val TEMP_FILES_DIR = "androidutils_tempfiles" // Directorio para almacenar los archivos temporales

    override val LOG_TAG = "TempFiles"

    /**
     * Elimina todos los archivos de la carpeta de archivos temporales (TEMP_FILES_DIR).
     * Se recomienda invocar esta función al iniciar la app, para no acumular muchos archivos, o bien invocar cuando
     * sea requerido limpiar la carpeta. El proceso se ejecuta en una corrutina con para no afectar el flujo de la app.
     * */
    fun clearTempFilesFolder(context: Context) {
        log("clearTempFilesFolder() invoked ")
        GlobalScope.launch(Dispatchers.IO) {
            try {
                makeTempDir(context) // Por si no existe el directorio, se genera para poder obtener la lista de archivos
                val files = File(context.filesDir, TEMP_FILES_DIR).listFiles()
                files?.forEach {
                    it.delete()
                }
                if (files != null && files.isNotEmpty()) log("Deleted ${files.size} file(s) from temp files dir")
                else log("Temp files dir is empty, no need to delete files")
                log("clearTempFilesFolder() done")
            } catch (e: Exception) {
                loge("Failed to delete files from temporary folder", e)
                firebaseCrashlyticsInstance?.recordException(e)
            }
        }
    }

    /**
     * Guarda la imagen en un archivo en la carpeta TEMP_FILES_DIR con formato PNG.
     * Para imágenes pequeñas el proceso es rápido y puede llamarse de manera síncrona,
     * para imágenes grandes se recomienda llamar de manera asíncrona.
     * **IMPORTANTE**: Llamar siempre dentro de un bloque try catch.
     * @param context Contexto.
     * @param bitmap Imagen tipo bitmap a guardar en el archivo.
     * @param fileName Nombre del archivo donde se va a guardar la imagen, el parámetro es opcional, si no se
     *        especifica o si se recibe una cadena en blanco, se genera un UUID para el nombre del archivo.
     * @return El archivo generado.
     * */
    fun saveBitmapToFile(context: Context, bitmap: Bitmap, fileName: String = ""): File {

        val finalFileName = if (fileName.trim().isBlank()) "${UUID.randomUUID()}.png" else "${fileName.trim()}.png"

        log("Saving bitmap [$finalFileName] into temp files dir")
        makeTempDir(context) // Se crea el directorio por si no existe

        // Se genera el archivo en la ruta de archivos de la app
        val file = File(context.filesDir, "$TEMP_FILES_DIR/$finalFileName")

        val stream = ByteArrayOutputStream() // Se crea un output stream para guardar el archivo
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream) // Se asigna el formato y la calidad y se mandan los datos al stream

        // Se escriben los datos en el archivo
        val fileOutPutStream = FileOutputStream(file)
        fileOutPutStream.write(stream.toByteArray())
        fileOutPutStream.close()

        log("Bitmap successfully saved [$file]")

        return file
    }

    /** Crea el directorio para archivos temporales (si aun no existe), invocar siempre antes de cualquier acción con archivos en esta clase */
    private fun makeTempDir(context: Context) {
        val result = File(context.filesDir, TEMP_FILES_DIR).mkdir() // Crea el directorio si no existe, si existe no realiza ninguna acción
        if (result) log("Directory for temporary files is created [${context.filesDir}/$TEMP_FILES_DIR]")
    }

}
