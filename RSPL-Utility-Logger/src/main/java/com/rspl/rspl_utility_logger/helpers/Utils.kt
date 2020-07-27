package com.rspl.rspl_utility_logger.helpers


import android.content.Context
import java.io.File
import java.io.FileNotFoundException


internal fun fileName(context: Context) = context.appName() + ".txt"

internal fun generateFileInInternalStorage(context: Context): File? {
    var file: File? = null
    val root = context.getExternalFilesDir(context.appName())
    var dirExists = true
    if (!root!!.exists()) {
        dirExists = root.mkdir()
    }
    if (dirExists) {
        file = File(root, fileName(context))
    }
    return file
}

// Method to delete Log file
fun deleteLogsFile(context: Context) {
    try {
        val root = context.getExternalFilesDir(context.appName())
        val file = File(root, fileName(context))
        if (file.exists())
            file.delete()
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
}