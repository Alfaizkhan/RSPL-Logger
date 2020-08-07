package com.rspl.rspl_utility_logger

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.rspl.rspl_utility_logger.`interface`.Logger
import com.rspl.rspl_utility_logger.helpers.appName
import com.rspl.rspl_utility_logger.helpers.fileName
import com.rspl.rspl_utility_logger.helpers.generateFileInInternalStorage
import com.rspl.rspl_utility_logger.services.FileUploadServiceClientBuilder
import com.rspl.rspl_utility_logger.services.FileUploadServiceResponse
import com.rspl.rspl_utility_logger.trees.DebugLogTree
import com.rspl.rspl_utility_logger.trees.FileLoggingTree
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File

object RSPLLogger : Logger {

    private var tree: Timber.Tree? = null

    private val fileUploadClient by lazy {
        FileUploadServiceClientBuilder.build()
    }

    fun startWithRSPLLogger(context: Context) {

        val file = generateFileInInternalStorage(context)

        if (file != null) {
            tree = FileLoggingTree(file).also {
                Timber.tag(context.appName())
                Timber.plant(it, DebugLogTree())
            }
        } else {
            Timber.e("Failed to create logs file")
        }
    }

    private fun isInitialised(): Boolean {
        if (tree == null) {
            Timber.plant(DebugLogTree())
        }
        return tree != null
    }

    private fun logNotInitialised() {
        Timber.e("Please initialise RSPLLogger\nstartWithRSPLLogger()")
    }

    override fun log(priority: Int, message: String) {
        if (isInitialised()) {
            when (priority) {
                Log.VERBOSE -> Timber.v(message)
                Log.DEBUG -> Timber.d(message)
                Log.INFO -> Timber.i(message)
                Log.WARN -> Timber.w(message)
                Log.ERROR, Log.ASSERT -> Timber.e(message)
                else -> Timber.v(message)
            }
        } else {
            logNotInitialised()
        }
    }

    fun verboseLog(message: String) {
        log(Log.VERBOSE, message)
    }

    fun infoLog(message: String) {
        log(Log.INFO, message)
    }

    fun debugLog(message: String) {
        log(Log.DEBUG, message)
    }

    fun warnLog(message: String) {
        log(Log.WARN, message)
    }

    fun errorLog(message: String) {
        log(Log.ERROR, message)
    }

    override fun sendLogToServer(context: Context, url: String) {
        val logFile = File(context.getExternalFilesDir(""), fileName(context))

        if (!logFile.exists()) {
            Toast.makeText(context, "No log file available in your device.", Toast.LENGTH_LONG)
                .show()
            return
        }

        val requestFile = logFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("logFile", logFile.name, requestFile)
        fileUploadClient.uploadLogFile(url, body)
            .enqueue(object : Callback<FileUploadServiceResponse> {

                override fun onResponse(
                    call: Call<FileUploadServiceResponse>,
                    response: Response<FileUploadServiceResponse>
                ) {
                    response.body()?.also {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<FileUploadServiceResponse>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Internal server error while uploading the log file.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}