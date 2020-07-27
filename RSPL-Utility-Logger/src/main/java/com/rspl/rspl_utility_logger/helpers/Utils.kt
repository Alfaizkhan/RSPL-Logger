package com.rspl.rspl_utility_logger.helpers


import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
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


// Method to open the Log File
fun openLogsFile(context: Context) {
    try {
        val root = context.filesDir
        val file = File(root, fileName(context))
        val authority = "${context.packageName}.com.rspl.rspl_utility_logger"
        val contentUri = FileProvider.getUriForFile(context, authority, file)
        if (file.exists()) {
            val mime = "text/html"
            // Open Log file
            val intent = Intent().also {
                it.action = Intent.ACTION_VIEW
                it.setDataAndType(contentUri, mime)
                it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "Logs file is empty!",
                Toast.LENGTH_SHORT).show()
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
}