package com.solo4.millionerquiz.data.database

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.solo4.millionerquiz.data.repositories.questions.LevelsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RemoteDBFetcher(
    private val preferences: SharedPreferences,
    private val levelsRepository: LevelsRepository
) {
    private var cachedDbVer: Int
        get() = preferences.getInt(CACHED_DB_VER_KEY, -1)
        set(value) = preferences.edit { putInt(CACHED_DB_VER_KEY, value) }

    suspend fun checkRemoteDatabaseVersion(): Boolean {
        return suspendCoroutine<Boolean> { cont ->
            Firebase.firestore.collection(APP_INFO_COLLECTION).document(DB_VER_DOCUMENT)
                .get()
                .addOnSuccessListener {
                    val remVer = it.data?.get(DB_LEVELS_VER_KEY)!! as Long
                    if (remVer > cachedDbVer) {
                        CoroutineScope(Dispatchers.IO).launch {
                            Log.e("RemoteDBFetcher", "Fetching levels...")
                            fetchLevelsData(remVer)
                            Log.e("RemoteDBFetcher", "Fetch success!")
                            cont.resume(true)
                        }
                    } else {
                        Log.e("RemoteDBFetcher", "Fetching is not necessery")
                        cont.resume(true)
                    }
                }
                .addOnFailureListener {
                    cont.resume(false)
                }
        }
    }

    private suspend fun fetchLevelsData(remVer: Long) {
        levelsRepository.getAllLevels()
        dbSource = Source.CACHE
        cachedDbVer = remVer.toInt()
    }

    companion object {

        var dbSource: Source = Source.SERVER; private set

        private const val CACHED_DB_VER_KEY = "CACHED_DB_VER_KEY"
        private const val APP_INFO_COLLECTION = "app_info"
        private const val DB_VER_DOCUMENT = "db_version"
        private const val DB_LEVELS_VER_KEY = "levels_version"
    }
}
