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
            }
        }
    }

    /**
     * Guarda la imagen [bitmap] en un archivo temporal en la carpeta TEMP_FILES_DIR, el nombre
     * del archivo es un UUID generado y en formato PNG, al final regresa el archivo creado. Para imágenes pequeñas
     * el proceso es rápido y puede llamarse de manera síncrona, para imágenes grandes se recomienda llamar de manera
     * asíncrona.
     * IMPORTANTE: Llamar siempre dentro de un bloque try catch.
     * */
    fun saveBitmapToFile(context: Context, bitmap: Bitmap): File {

        val fileName = "${UUID.randomUUID()}.png" // Se genera un nombre para el archivo

        log("Saving bitmap [$fileName] into temp files dir")
        makeTempDir(context) // Se crea el directorio por si no existe

        // Se genera el archivo en la ruta de archivos de la app
        val file = File(context.filesDir, "$TEMP_FILES_DIR/$fileName")

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