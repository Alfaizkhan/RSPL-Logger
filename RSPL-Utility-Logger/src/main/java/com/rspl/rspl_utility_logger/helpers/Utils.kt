package com.rspl.rspl_utility_logger.helpers


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileNotFoundException


internal fun fileName(context: Context) = context.appName() + ".txt"

internal fun generateFileInInternalStorage(context: Context): File? {
    var file: File? = null
    val root = context.getExternalFilesDir("")
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
        val root = context.getExternalFilesDir("")
        val file = File(root, fileName(context))
        if (file.exists())
            file.delete()
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
}

// Method to Share Log file Over email
fun shareLogsFile(activity: Activity, emailAddress: String) {
    val context = activity.applicationContext
    try {
        val root = context.getExternalFilesDir("")
        val file = File(root, fileName(context))
        if (file.exists()) {
            val authority = "${context.packageName}.com.rspl.rspl_utility_logger"
            val contentUri = FileProvider.getUriForFile(context, authority, file)
            val intent = ShareCompat.IntentBuilder.from(activity)
                .setType("text/plain")
                .setSubject("Share logs file")
                .setStream(contentUri)
                .setChooserTitle("${context.appName()} logs")
                .setEmailTo(arrayOf(emailAddress))
                .setSubject("[LOGS] ${context.appName()}")
                .createChooserIntent()
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(Intent.createChooser(intent, "Share logs file"))
        } else {
            Toast.makeText(activity, "Logs file is empty!", Toast.LENGTH_SHORT).show()
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
}
