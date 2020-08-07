package com.rspl.rspl_utility_logger.`interface`

import android.content.Context

interface Logger {
    fun log(priority: Int , message: String)
    fun sendLogToServer(context: Context, url : String)
}