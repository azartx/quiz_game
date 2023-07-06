package com.solo4.millionerquiz.data.database.deserializers

import androidx.annotation.WorkerThread
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import com.solo4.millionerquiz.model.game.Level
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
}