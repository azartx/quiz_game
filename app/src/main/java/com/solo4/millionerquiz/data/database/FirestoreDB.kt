package com.solo4.millionerquiz.data.database

import android.util.Log
import androidx.annotation.WorkerThread
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.solo4.millionerquiz.data.database.deserializers.FirebaseDBDataDeserializer
import com.solo4.millionerquiz.data.settings.language.LanguageManager
import com.solo4.millionerquiz.model.auth.User
import com.solo4.millionerquiz.model.game.Level
import com.solo4.millionerquiz.model.score.UserScoreItem
import org.koin.core.component.KoinComponent
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Suppress("SpellCheckingInspection")
class FirestoreDB(
    private val deserializer: FirebaseDBDataDeserializer,
    private val langManager: LanguageManager
) : KoinComponent {
    private val firestore by lazy { Firebase.firestore }

    @WorkerThread
    suspend fun getAllLevelsData(): List<Level> = suspendCoroutine { cont ->
        firestore.collection(langManager.levelsLanguage.levelsDbName)
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
    suspend fun getAllScoresData(): List<UserScoreItem> = suspendCoroutine { cont ->
        firestore.collection(COLLECTION_SCORES)
            .get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    cont.resume(deserializer.documentsToScoreItems(collection.documents))
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
        firestore.collection(langManager.levelsLanguage.levelsDbName)
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
        firestore.collection(langManager.levelsLanguage.levelsDbName)
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

    suspend fun updateUserScore(user: User, score: Int) {
        val previousScore = suspendCoroutine<Int> { cont ->
            firestore
                .collection(COLLECTION_SCORES)
                .document(user.id)
                .get()
                .addOnSuccessListener {
                    cont.resume(deserializer.documentToScoreItem(it)?.score ?: 0)
                }
                .addOnFailureListener {
                    cont.resume(0)
                }
        }

        firestore.collection(COLLECTION_SCORES)
            .document(user.id)
            .set(
                // deserializer.scoreItemToJson(
                UserScoreItem(user.id, user.name, previousScore + score)
                // )
            )
    }

    private companion object {
        const val TAG = "FirestoreDb"
        const val COLLECTION_SCORES = "score"
    }
}
