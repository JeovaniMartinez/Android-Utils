package com.jeovanimartinez.androidutils.filesystem

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import androidx.annotation.IntRange
import com.jeovanimartinez.androidutils.Base
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/** Set of utilities for working with the file system. */
object FileUtils : Base<FileUtils>() {

    override val LOG_TAG = "FileUtils"

    /**
     * Analyze and generate a final name for a file according to the received parameters.
     * ```
     *     // Example of the file name based on the received parameters.
     *     // ** NOTE: An empty or blank string will be interpreted as null.
     *     // ** NOTE: Whitespace is removed from the parameter strings if they are not null.
     *     (fileName = null, fileExtension = null) // f067ee7e-7875-4e4a-9f3b-ddddddf365e5
     *     (fileName = "demo", fileExtension = null) // demo
     *     (fileName = "demo.txt", fileExtension = null) // demo.txt
     *     (fileName = "demo", fileExtension = "txt") // demo.txt
     *     (fileName = "demo.txt", fileExtension = "txt") // demo.txt.txt
     *     (fileName = null, fileExtension = "txt") // c1c53230-d6b7-4216-a8a3-a12eb1aec165.txt
     * ```
     * @param fileName File name, set as null or an empty or blank string to use a UUID as the file name.
     * @param fileExtension File extension. Set as null or an empty or blank string to omit the extension,
     *        or if the extension is already included in the [fileName].
     * @return The final normalized filename.
     * */
    fun generateNormalizedFileName(fileName: String?, fileExtension: String?): String {

        var finalFileName = if (fileName != null && fileName.trim().isNotBlank()) fileName.trim() else UUID.randomUUID().toString()
        if (fileExtension != null && fileExtension.trim().isNotBlank()) finalFileName = "${finalFileName}.${fileExtension.trim()}"

        return finalFileName

    }

    /**
     * Save a bitmap object in an image file.
     * @param context Context.
     * @param bitmap Bitmap to be saved in the file.
     * @param fileName Filename for the image (without the extension as it is added automatically).
     *        Set to null or an empty or blank string to generate a UUID as the file name.
     * @param format Format for the image based on [Bitmap.CompressFormat].
     * @param path Absolute path where the image will be saved. If null or an empty or blank string, the image
     *   will be saved in the app's private storage within the temporary files directory of the [TempFileManager] utility.
     * @param quality Quality for the image, between 0 and 100. Default is 100. The value is interpreted differently
     *   depending on the [Bitmap.CompressFormat]
     *
     * @return The created file.
     *
     * @throws IOException If an I/O error occurs.
     * */
    @Throws(IOException::class)
    fun saveBitmapToFile(
        context: Context,
        bitmap: Bitmap,
        fileName: String?,
        format: CompressFormat,
        path: String?,
        @IntRange(from = 0, to = 100) quality: Int = 100
    ): File {

        // Get the file extension according the format
        val fileExtension = when (format) {
            CompressFormat.JPEG -> "jpeg"
            CompressFormat.PNG -> "png"
            else -> "webp"
        }

        val finalFileName = generateNormalizedFileName(fileName, fileExtension) // Adjust the file name

        val file = generateFile(context, finalFileName, path) // Generate the file

        log("Saving bitmap [$finalFileName] in [$file]")

        val stream = ByteArrayOutputStream()
        bitmap.compress(format, quality, stream) // Write the bitmap to the output stream

        // Write the file
        val fileOutPutStream = FileOutputStream(file)
        fileOutPutStream.write(stream.toByteArray())
        fileOutPutStream.close()
        stream.close()

        log("Bitmap saved successfully")

        return file

    }

    /**
     * Generate an abstract file to write content to it later.
     * @param context Context.
     * @param fileName Name for the file, must include the extension.
     * @param path Absolute path to create the file, if it is null, it is created in the temp files directory of the [TempFileManager] utility.
     * @return The newly generated file.
     * */
    private fun generateFile(context: Context, fileName: String, path: String?): File {

        /*
        * No I/O exceptions occur when generating a file with File(parent, child), or when called File(path).mkdirs()
        * since it is generated in an abstract manner, and exceptions may arise only once content is written to the file.
        * */

        // Use a custom path if the path is not null and, after trimming, it is not empty
        val useCustomPath = path != null && path.trim().isNotBlank()

        val file = if (!useCustomPath) TempFileManager.createNewTempFile(context, fileName) // If the path is null, use the temp files dir
        else File(path, fileName) // Otherwise uses the defined path

        // If using a custom path, the directories are created if they don't exist so that the file can be written correctly
        if (useCustomPath) {
            val dirPath = file.absolutePath.substringBeforeLast("/") // Get the path, exclude the filename
            val dirFile = File(dirPath)
            if (!dirFile.exists()) {
                File(dirPath).mkdirs()
                log("Make directory [$dirPath]")
            }
        }

        return file
    }

}
