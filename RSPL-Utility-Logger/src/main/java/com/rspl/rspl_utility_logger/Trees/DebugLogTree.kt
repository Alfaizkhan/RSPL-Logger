package com.rspl.rspl_utility_logger.Trees

import android.util.Log
import timber.log.Timber
import kotlin.math.min

class DebugLogTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (isLoggable(tag, priority)) {
            if (message.length < MAX_LOG_LENGTH) {
                if (priority == Log.ASSERT) {
                    Timber.e(message)
                } else {
                    Log.println(priority, LOG_TAG, message)
                }
                return
            }

            var i = 0
            val length = message.length
            while (i < length) {
                var newline = message.indexOf('\n', i)
                newline = if (newline != -1) newline else length
                do {
                    val end = min(newline, i + MAX_LOG_LENGTH)
                    val part = message.substring(i, end)
                    if (priority == Log.ASSERT) {
                        Timber.e(message)
                    } else {
                        Log.println(priority, LOG_TAG, part)
                    }
                    i = end
                } while (i < newline)
                i++
            }
        }
    }

    companion object {
        private const val MAX_LOG_LENGTH = 5000
        const val LOG_TAG = "RSPLLogger"
    }
}