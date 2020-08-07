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

    internal fun generateFileInInternalStorage(context: Context): File? =
    context.getExternalFilesDir("")?.let {
        var dirExists = true
        if (!it.exists()) {
            dirExists = it.mkdir()
        }

        if (dirExists) {
            File(it, fileName(context))
        } else {
            null
        }
    }

// Method to delete Log file
fun deleteLogsFile(context: Context) {
    try {
        context.getExternalFilesDir("")?.also {
            val file = File(it, fileName(context))
            if (file.exists()) {
                file.delete()
            }
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
}

// Method to Share Log file Over email
fun shareLogsFile(context: Context, emailAddress: String) {
    val appContext = context.applicationContext
    try {
        val root = appContext.getExternalFilesDir("")
        val file = File(root, fileName(appContext))
        if (file.exists()) {
            val authority = "${appContext.packageName}.com.rspl.rspl_utility_logger"
            val contentUri = FileProvider.getUriForFile(appContext, authority, file)
            val intent = ShareCompat.IntentBuilder.from(context as Activity)
                .setType("text/plain")
                .setStream(contentUri)
                .setChooserTitle("${appContext.appName()} logs")
                .setEmailTo(arrayOf(emailAddress))
                .setSubject("[LOGS] ${appContext.appName()}")
                .createChooserIntent()
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(Intent.createChooser(intent, "Share logs file"))
        } else {
            Toast.makeText(context, "Logs file is empty!", Toast.LENGTH_SHORT).show()
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
}
