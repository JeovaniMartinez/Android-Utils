package com.jeovanimartinez.androidutils.filesystem

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import com.jeovanimartinez.androidutils.Base
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/** Set of utilities for working with files. */
object FileUtils : Base<FileUtils>() {

    override val LOG_TAG = "FileUtils"

    /**
     * Save a bitmap object to a file.
     * @param context Context.
     * @param bitmap Bitmap to save.
     * @param fileName Filename for the image, if it is null or blank string, a UUID is used for the image file name.
     *        If the file name does not contain the file extension, it is automatically added. So it is recommended
     *        that it be the file name without the extension.
     * @param path Absolute path to save the image file, if it is null, it is saved in the temp files directory.
     * @param format Format for the image. Default is PNG.
     * @param quality Quality for the image, between 0 and 100. Default is 100.
     *
     * @throws IOException If an I/O error occurs.
     * */
    @Throws(IOException::class)
    fun saveBitmapToFile(
        context: Context,
        bitmap: Bitmap,
        fileName: String? = null,
        path: String? = null,
        format: CompressFormat = CompressFormat.PNG,
        quality: Int = 100
    ): File {

        // Get the file extension for the format
        val fileExtension = when (format) {
            CompressFormat.PNG -> "png"
            CompressFormat.JPEG -> "jpeg"
            else -> "webp"
        }

        val finalFileName = normalizeFileName(fileName, fileExtension) // Adjust the file name

        val file = generateFile(context, finalFileName, path) // Generate the file

        log("Saving bitmap [$finalFileName] into [$file]")

        val stream = ByteArrayOutputStream()
        bitmap.compress(format, quality, stream) // Adjust bitmap

        // Write the file
        val fileOutPutStream = FileOutputStream(file)
        fileOutPutStream.write(stream.toByteArray())
        fileOutPutStream.close()

        log("Bitmap saved successfully")

        return file

    }

    /**
     * Analyze and generate the final name for the file including its extension.
     * - If the [fileName] is null or blank, it generate a UUID with the [fileExtension] for the file name.
     * - If the [fileExtension] already contains the [fileExtension], it is left as this.
     * - Otherwise the [fileExtension] is added to the [fileName].
     * @param fileName Name for the file.
     * @param fileExtension Extension for the file.
     * @return A final file name with the extension.
     * */
    private fun normalizeFileName(fileName: String?, fileExtension: String): String {
        return if (fileName == null || fileName.trim().isBlank()) "${UUID.randomUUID()}.$fileExtension" // Default, use UUID
        else {
            if (fileName.trim().toLowerCase(Locale.ROOT).endsWith(fileExtension)) fileName // If already contains file extension,
            else "${fileName.trim()}.$fileExtension" // Add the file extension
        }
    }

    /**
     * Generate an abstract file.
     * @param context Context.
     * @param fileName Name for the file, must include the extension.
     * @param path Absolute path to create the file, if it is null, it is created in the temp files directory.
     * */
    private fun generateFile(context: Context, fileName: String, path: String?): File {
        // Generate file in the path
        val file = if (path == null) File(context.filesDir, "${TempFiles.TEMP_FILES_DIR}/$fileName") // If path is null, use the temp files dir
        else File(path, fileName) // Otherwise uses the defined path

        if (path == null) TempFiles.makeTempDir(context) // If uses temp file dir, should be created it if doesn't exist
        else {
            // If use a custom path, the directories are created if they don't exist
            val dirPath = file.absolutePath.substringBeforeLast("/") // Get the path exclude filename
            log("Make directory [$dirPath]")
            File(dirPath).mkdirs()
        }

        return file
    }

}
