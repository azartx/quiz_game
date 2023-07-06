package com.solo4.millionerquiz.data.database

import android.util.Log
import androidx.annotation.WorkerThread
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.solo4.millionerquiz.data.database.deserializers.FirebaseDBDataDeserializer
import com.solo4.millionerquiz.model.game.Level
import org.koin.core.component.KoinComponent
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Suppress("SpellCheckingInspection")
class FirestoreDB(private val deserializer: FirebaseDBDataDeserializer) : KoinComponent {
    private val firestore by lazy { Firebase.firestore }

    @WorkerThread
    suspend fun getAllLevelsData(): List<Level> = suspendCoroutine { cont ->
        firestore.collection(COLLECTION_LEVELS)
            .get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    cont.resume(deserializer.documentsToLevels(collection.documents))
                } else {
                    Log.e(TAG, "Firebase collection is null")
                    cont.resume(listOf())
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Exception while getting levels from firebase:", it)
                cont.resume(listOf())
            }
    }

    @WorkerThread
    suspend fun getLevelsCount(): Int = suspendCoroutine { cont ->
        firestore.collection(COLLECTION_LEVELS)
            .count().query.get()
            .addOnSuccessListener { cont.resume(it?.size() ?: 0) }
            .addOnFailureListener {
                Log.e(TAG, "Exception while getting levels count:", it)
                cont.resume(0)
            }
    }

    @WorkerThread
    @Throws(IndexOutOfBoundsException::class, RuntimeException::class)
    suspend fun getLevelData(levelNumber: Int): Level = suspendCoroutine { cont ->
        firestore.collection(COLLECTION_LEVELS)
            .document("/$levelNumber")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot == null) {
                    cont.resumeWithException(IndexOutOfBoundsException("Level $levelNumber is not exist"))
                } else {
                    val doc = deserializer.documentToLevel(documentSnapshot)
                    if (doc == null) {
                        cont.resumeWithException(IndexOutOfBoundsException("Level $levelNumber is not exist"))
                    } else {
                        cont.resume(doc)
                    }
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Exception while getting level $levelNumber:", it)
                cont.resumeWithException(
                    RuntimeException("Exception while getting level $levelNumber:", it)
                )
            }
    }

    private companion object {
        const val TAG = "FirestoreDb"
        const val COLLECTION_LEVELS = "levels"
    }
}
