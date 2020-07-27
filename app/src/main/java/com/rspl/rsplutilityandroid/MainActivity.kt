package com.rspl.rsplutilityandroid

import android.app.Activity
import android.os.Bundle
import com.rspl.rspl_utility_logger.RSPLLogger
import com.rspl.rspl_utility_logger.RSPLLogger.debugLog
import com.rspl.rspl_utility_logger.RSPLLogger.errorLog
import com.rspl.rspl_utility_logger.RSPLLogger.infoLog
import com.rspl.rspl_utility_logger.RSPLLogger.verboseLog
import com.rspl.rspl_utility_logger.RSPLLogger.warnLog
import com.rspl.rspl_utility_logger.helpers.deleteLogsFile
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        RSPLLogger.startWithRSPLLogger(applicationContext)

        verboseLog("RSPL Verbos")
        debugLog("RSPL Debug")
        infoLog("RSPL info")
        warnLog("RSPL warn")
        errorLog("RSPL Error")


        btn_delete.setOnClickListener {
            deleteLogsFile()
        }

        btn_upload.setOnClickListener {
            RSPLLogger.sendLogToServer(this, "http://ptsv2.com/t/mickey/post")
        }
    }
}