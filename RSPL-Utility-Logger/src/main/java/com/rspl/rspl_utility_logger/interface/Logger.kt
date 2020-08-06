package com.rspl.rspl_utility_logger.`interface`

import android.content.Context

interface Logger {
    fun verboseLog(message: String)
    fun infoLog(message: String)
    fun debugLog(message: String)
    fun warnLog(message: String)
    fun errorLog(message:String)
    fun sendLogToServer(context: Context, url : String)
}