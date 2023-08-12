package com.jeovanimartinez.androidutils.filesystem

import android.content.Context
import com.jeovanimartinez.androidutils.Base
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

/** Utility to work with temporary files. */
object TempFileManager : Base<TempFileManager>() {

    /** Directory to store temporary files, the parent is context.filesDir (private app directory) */
    const val TEMP_FILES_DIR = "androidutils/tempfiles"

    override val LOG_TAG = "TempFileManager"

    /**
     * Delete all files created with this utility (the files in the temporary folder), it is recommended to call at the
     * start of the app or when you need to clean the contents of the folder. The function runs asynchronously so as
     * not to affect the app flow. It's not necessary to call within a try-catch block, as exceptions are handled internally.
     * @param context Context.
     * @param onComplete Optional callback function that will be invoked when the operation is complete. It will receive a
     *        boolean parameter indicating the success of the operation.
     * */
    @OptIn(DelicateCoroutinesApi::class)
    fun clearTempFilesFolder(context: Context, onComplete: (success: Boolean) -> Unit = {}) {

        log("clearTempFilesFolder() invoked")

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val dir = File(context.filesDir, TEMP_FILES_DIR)
                val fileCount = dir.listFiles()?.size ?: 0
                dir.deleteRecursively() // Delete all files recursively

                // The directory is created again; makeTempDir() is not used to avoid shown logs
                dir.mkdirs()

                if (fileCount > 0) log("Deleted $fileCount file(s) from the temp files dir")
                else log("The temp files dir is empty, no need to delete files")

                log("clearTempFilesFolder() done")
                onComplete(true)

            } catch (e: Exception) {
                loge("Failed to delete the files from the temporary directory", e)
                firebaseCrashlyticsInstance?.recordException(e)
                onComplete(false)
            }
        }
    }

    /**
     * Create a new file in the temporary files directory. The file will be generated in the file system until some
     * content is saved to it, and it will be deleted when [clearTempFilesFolder] is called.
     * ```
     *     // Example of the file name based on the received parameters.
     *     // ** NOTE: An empty string will be interpreted as null, and string are trimmed **
     *     (fileName = null, fileExtension = null) // f067ee7e-7875-4e4a-9f3b-ddddddf365e5
     *     (fileName = "demo", fileExtension = null) // demo
     *     (fileName = "demo.txt", fileExtension = null) // demo.txt
     *     (fileName = "demo", fileExtension = "txt") // demo.txt
     *     (fileName = "demo.txt", fileExtension = "txt") // demo.txt.txt
     *     (fileName = null, fileExtension = "txt") // c1c53230-d6b7-4216-a8a3-a12eb1aec165.txt
     * ```
     * @param context Context.
     * @param fileName File name to create, set as null to use a UUID as the file name, which is highly recommended
     *        for the temp files folder.
     * @param fileExtension File extension. Set as null to create a file without extension, or if the extension is
     *        already included in the [fileName].
     * @return The newly created temporary file.
     * */
    fun createNewTempFile(context: Context, fileName: String? = null, fileExtension: String? = null): File {

        // Generate the final file name
        var finalFileName = if (fileName != null && fileName.trim().isNotEmpty()) fileName.trim() else UUID.randomUUID().toString()
        if (fileExtension != null && fileExtension.trim().isNotEmpty()) finalFileName = "${finalFileName}.${fileExtension.trim()}"

        makeTempDir(context) // It is always called before creating the file to ensure that the directory exists.

        // Creates and return the new file
        return File(context.filesDir, "$TEMP_FILES_DIR/$finalFileName")

    }

    /**
     * Create the directory for temporary files (if it does not already exist). Invoke whenever you are going to work with the
     * temporary files directory.
     * @param context Context.
     * */
    private fun makeTempDir(context: Context) {
        log("makeTempDir() invoked")
        val result = File(context.filesDir, TEMP_FILES_DIR).mkdirs() // Create the directory if it does not exist. If it exists, do not take any action.
        if (result) log("The directory for temporary files is created [${context.filesDir}/$TEMP_FILES_DIR]")
        else log("The directory for temporary files already exists, there's no need to create it [${context.filesDir}/$TEMP_FILES_DIR]")
    }

}
