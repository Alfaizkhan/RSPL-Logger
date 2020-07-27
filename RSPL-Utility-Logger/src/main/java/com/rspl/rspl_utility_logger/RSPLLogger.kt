package com.rspl.rspl_utility_logger

import android.content.Context
import com.rspl.rspl_utility_logger.Interface.Logger
import com.rspl.rspl_utility_logger.Trees.DebugLogTree
import com.rspl.rspl_utility_logger.Trees.FileLoggingTree
import com.rspl.rspl_utility_logger.helpers.*
import com.rspl.rspl_utility_logger.helpers.appName
import com.rspl.rspl_utility_logger.helpers.fileName
import timber.log.Timber
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.math.min


object RSPLLogger: Logger{

    private var tree: Timber.Tree? = null
    private var response = "Error"
    private const val lineEnd = "\r\n"
    private const val twoHyphens = "--"
    private val boundary = UUID.randomUUID().toString()
    private val token = Math.random()
    private val taskId = Math.random()


    fun startWithRSPLLogger(context: Context) {

        val file = generateFileInInternalStorage(context)

        if (file != null) {
            tree = FileLoggingTree(file)
            tree?.let{
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

    override fun verboseLog(message: String) {
        if (isInitialised()) {
            Timber.v(message)
        } else {
            logNotInitialised()
        }
    }

    override fun infoLog(message: String) {
        if (isInitialised()) {
            Timber.i(message)
        } else {
            logNotInitialised()
        }
    }

    override fun debugLog(message: String) {
        if (isInitialised()) {
            Timber.d(message)
        } else {
            logNotInitialised()
        }
    }

    override fun warnLog(message: String) {
        if (isInitialised()) {
            Timber.w(message)
        } else {
            logNotInitialised()
        }
    }

    override fun errorLog(message: String) {
        if (isInitialised()) {
            Timber.e(message)
        } else {
            logNotInitialised()
        }
    }

    override fun sendLogToServer(context: Context, url: String): String? {

        Thread(Runnable {
            // Do network action in this function
            val connection: HttpURLConnection?
            val root = context.getExternalFilesDir(context.appName())
            val pathToOurFile = File(root, fileName(context))

            // If file not created or deleted by user
            if (!pathToOurFile.exists()){
                generateFileInInternalStorage(context)
            }

            val urlServer: String = url
            var bytesRead: Int
            var bytesAvailable: Int
            var bufferSize: Int
            val buffer: ByteArray
            val maxBufferSize = 1 * 1024
            try {
                val fileInputStream = FileInputStream(pathToOurFile)
                val targetUrl = URL(urlServer)
                connection = targetUrl.openConnection() as HttpURLConnection

                // Allow Inputs & Outputs
                connection.doInput = true
                connection.doOutput = true
                connection.useCaches = false
                connection.setChunkedStreamingMode(1024)

                // Enable POST method
                connection.requestMethod = "POST"
                connection.setRequestProperty("Connection", "Keep-Alive")
                connection.setRequestProperty("ENCTYPE", "multipart/form-data")
                connection.setRequestProperty(
                    "Content-Type",
                    "multipart/form-data; boundary=$boundary"
                )

                val opStream = DataOutputStream(connection.outputStream)
                opStream.writeBytes(twoHyphens + boundary + lineEnd)

                opStream.writeBytes("Content-Disposition: form-data; name=\"Token\"$lineEnd")
                opStream.writeBytes("Content-Type: text/plain;charset=UTF-8$lineEnd")
                opStream.writeBytes("Content-Length: $token$lineEnd")
                opStream.writeBytes(lineEnd)
                opStream.writeBytes("$token" + lineEnd)
                opStream.writeBytes(twoHyphens + boundary + lineEnd)

                opStream.writeBytes("Content-Disposition: form-data; name=\"TaskID\"$lineEnd")
                opStream.writeBytes("Content-Type: text/plain;charset=UTF-8$lineEnd")
                opStream.writeBytes("Content-Length: $taskId$lineEnd")
                opStream.writeBytes(lineEnd)
                opStream.writeBytes("$taskId" + lineEnd)
                opStream.writeBytes(twoHyphens + boundary + lineEnd)


                var connStr: String? = null
                connStr = ("Content-Disposition: form-data; name=\"UploadFile\";filename=\""
                        + pathToOurFile + "\"" + lineEnd)

                opStream.writeBytes(connStr)
                opStream.writeBytes(lineEnd)

                bytesAvailable = fileInputStream.available()
                bufferSize = min(bytesAvailable, maxBufferSize)
                buffer = ByteArray(bufferSize)

                // Read file
                bytesRead = fileInputStream.read(buffer, 0, bufferSize)
                try {
                    while (bytesRead > 0) {
                        try {
                            opStream.write(buffer, 0, bufferSize)
                        } catch (e: OutOfMemoryError) {
                            e.printStackTrace()
                            response = "OutOfMemoryError"
                            response
                        }
                        bytesAvailable = fileInputStream.available()
                        bufferSize = min(bytesAvailable, maxBufferSize)
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    response = "error"
                    response
                }
                opStream.writeBytes(lineEnd)
                opStream.writeBytes(
                    twoHyphens + boundary + twoHyphens
                            + lineEnd
                )

                // Responses from the server (code and message)
                val serverResponseCode = connection.responseCode
                val serverResponseMessage = connection.responseMessage
                println("Server Response Code  $serverResponseCode")
                println("Server Response Message $serverResponseMessage")
                response = if (serverResponseCode == 200) {
                    "true"
                } else {
                    "false"
                }

                fileInputStream.close()
                opStream.flush()
                connection.inputStream

                //for android InputStream
                val inputStm = connection.inputStream
                var char: Int
                val b = StringBuffer()
                while (inputStm.read().also { char = it } != -1) {
                    b.append(char.toChar())
                }
                val responseString = b.toString()
                println("response string is $responseString")
                opStream.close()
            } catch (ex: Exception) {
                // Exception handling
                response = "error"
                println("Send file Exception" + ex.message + "")
                ex.printStackTrace()
            }
        }).start()
        return response
    }
}