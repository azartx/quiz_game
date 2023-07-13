package com.solo4.millionerquiz.data.database.deserializers

import androidx.annotation.WorkerThread
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import com.solo4.millionerquiz.model.game.Level
import com.solo4.millionerquiz.model.score.UserScoreItem
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FirebaseDBDataDeserializer(private val gson: Gson) : KoinComponent {
    @WorkerThread
    fun documentsToLevels(documents: List<DocumentSnapshot>): List<Level> {
        return documents
            .map { it.data as? HashMap<String, ArrayList<HashMap<String, Any>>> ?: return listOf() }
            .map { gson.fromJson(gson.toJson(it), Level::class.java) }
    }

    @WorkerThread
    fun documentToLevel(document: DocumentSnapshot): Level? {
        val doc =  document.data as? HashMap<String, ArrayList<HashMap<String, Any>>>
        return gson.fromJson(gson.toJson(doc), Level::class.java)
    }

    @WorkerThread
    fun documentsToScoreItems(documents: List<DocumentSnapshot>): List<UserScoreItem> {
        return documents
            .map { it.data as? HashMap<String, HashMap<String, Any>> ?: return listOf() }
            .map { gson.fromJson(gson.toJson(it), UserScoreItem::class.java) }
    }

    @WorkerThread
    fun documentToScoreItem(document: DocumentSnapshot): UserScoreItem? {
        val doc = document.data as? HashMap<String, HashMap<String, Any>> ?: return null
        return gson.fromJson(gson.toJson(doc), UserScoreItem::class.java)
    }

    @WorkerThread
    fun scoreItemToJson(item: UserScoreItem): String {
        return gson.toJson(item)
    }
}