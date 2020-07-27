package com.rspl.rspl_utility_logger.helpers

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import timber.log.Timber

internal fun Context.appName(): String {
    val packageManager = packageManager
    var applicationInfo: ApplicationInfo? = null
    try {
        applicationInfo =
            packageManager.getApplicationInfo(getApplicationInfo().packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        Timber.e(e)
    }
    return if (applicationInfo != null)
        packageManager.getApplicationLabel(applicationInfo) .toString()
    else
        "UnknownAppName"
}
