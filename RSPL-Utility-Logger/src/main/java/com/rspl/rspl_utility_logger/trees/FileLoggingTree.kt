package com.rspl.rspl_utility_logger.trees

import android.util.Log
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class FileLoggingTree(private val file: File) : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        try {
            val logTimeStamp = SimpleDateFormat(
                "yyy-MM-dd' - 'HH:mm:ss:SSS ",
                Locale.getDefault()
            ).format(Date())

            val color =  when (priority) {
                Log.VERBOSE -> "#424242"
                Log.DEBUG -> "#2196F3"
                Log.INFO -> "#4CAF50"
                Log.WARN -> "#FFC107"
                Log.ERROR-> "#F44336"
                else -> "#FFAB00"
            }

            val priorityTag = when (priority) {
                Log.VERBOSE -> "VERBOSE"
                Log.DEBUG -> "DEBUG"
                Log.INFO -> "INFO"
                Log.WARN -> "WARN"
                Log.ERROR-> "ERROR"
                else -> "#FFFFFF"
            }

            // If file created or exists save logs
            val writer = FileWriter(file, true)
            writer
                .append(" $tag >>> ")
                .append("$logTimeStamp, ")
                .append("$priorityTag, ")
                .append("$message |" )
                .append("\n\r")
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            Timber.e("Error while logging into file : $e")
        }
    }

    override fun createStackElementTag(element: StackTraceElement): String? {
        // Add log statements line number to the log
        return super.createStackElementTag(element) + "@" + element.lineNumber
    }
}