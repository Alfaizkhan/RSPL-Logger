package com.rspl.rsplutilityandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rspl.rspl_utility_logger.RSPLLogger
import com.rspl.rspl_utility_logger.RSPLLogger.debugLog
import com.rspl.rspl_utility_logger.RSPLLogger.errorLog
import com.rspl.rspl_utility_logger.RSPLLogger.infoLog
import com.rspl.rspl_utility_logger.RSPLLogger.verboseLog
import com.rspl.rspl_utility_logger.RSPLLogger.warnLog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RSPLLogger.startWithRSPLLogger(applicationContext)

        verboseLog("RSPL Verbos")
        debugLog("RSPL Debug")
        infoLog("RSPL info")
        warnLog("RSPL warn")
        errorLog("RSPL Error")

        RSPLLogger.sendLogToServer(this,"http://ptsv2.com/t/mickey/post")
    }
}