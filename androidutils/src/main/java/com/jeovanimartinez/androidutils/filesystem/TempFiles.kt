@file:Suppress("unused")

package com.jeovanimartinez.androidutils.filesystem

import android.content.Context
import com.jeovanimartinez.androidutils.Base
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

/** Utility to work with temporary files. */
object TempFiles : Base<TempFiles>() {

    /** Directory to store temporary files, the parent is context.filesDir */
    const val TEMP_FILES_DIR = "androidutils/tempfiles"

    override val LOG_TAG = "TempFiles"

    /**
     * Delete all files created with this utility (the files in the temporary folder), it is recommended to call at the
     * start of the app or when you need to clean the contents of the folder. The function runs asynchronously so as
     * not to affect the app flow and can be called from anywhere.
     * */
    fun clearTempFilesFolder(context: Context) {
        log("clearTempFilesFolder() invoked")
        GlobalScope.launch(Dispatchers.IO) {
            try {
                makeTempDir(context) // If not exists, it is generated to be able to get the file list.
                val files = File(context.filesDir, TEMP_FILES_DIR).listFiles()
                files?.forEach {
                    it.delete()
                }
                if (files != null && files.isNotEmpty()) log("Deleted ${files.size} file(s) from temp files dir")
                else log("Temp files dir is empty, no need to delete files")
                log("clearTempFilesFolder() done")
            } catch (e: Exception) {
                loge("Failed to delete files from the temporary folder", e)
                firebaseCrashlyticsInstance?.recordException(e)
            }
        }
    }

    /**
     * Create the directory for temporary files (if it does not already exist). Invoke whenever you are going to work with the
     * temporary files directory.
     * */
    fun makeTempDir(context: Context) {
        val result = File(context.filesDir, TEMP_FILES_DIR).mkdirs() // Create the directory if it does not exist. If it exists, do not take any action.
        if (result) log("Directory for temporary files is created [${context.filesDir}/$TEMP_FILES_DIR]")
    }

}
