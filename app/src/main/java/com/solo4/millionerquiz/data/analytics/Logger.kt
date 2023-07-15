package com.solo4.millionerquiz.data.analytics

import android.util.Log
import androidx.core.os.bundleOf
import com.solo4.millionerquiz.BuildConfig

object Logger {

    private val analytics = AnalyticsManager()

    fun e(
        tag: String,
        message: String,
        analyticsEvent: String = Event.none.name,
        tr: Throwable? = null
    ) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, tr)
        }
        analytics.report(bundleOf(tag to message), analyticsEvent)
    }
}
