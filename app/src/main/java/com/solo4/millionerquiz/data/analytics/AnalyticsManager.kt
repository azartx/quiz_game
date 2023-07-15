package com.solo4.millionerquiz.data.analytics

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class AnalyticsManager {
    private val firebaseAnalytics = Firebase.analytics

    fun report(params: Bundle? = null, event: String = Event.none.name) {
        firebaseAnalytics.logEvent(event, params)
    }
}
