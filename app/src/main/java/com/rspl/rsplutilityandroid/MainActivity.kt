package com.rspl.rsplutilityandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rspl.rspl_utility_logger.RsplLogger
import com.rspl.rspl_utility_logger.RsplLogger.debugLog
import com.rspl.rspl_utility_logger.RsplLogger.errorLog
import com.rspl.rspl_utility_logger.RsplLogger.infoLog
import com.rspl.rspl_utility_logger.RsplLogger.verboseLog
import com.rspl.rspl_utility_logger.RsplLogger.warnLog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RsplLogger.startWithRSPLLogger(applicationContext)

        verboseLog("RSPL Verbos")
        debugLog("RSPL Debug")
        infoLog("RSPL info")
        warnLog("RSPL warn")
        errorLog("RSPL Error")

        RsplLogger.sendLogToServer(this,"http://ptsv2.com/t/mickey/post")
    }
}