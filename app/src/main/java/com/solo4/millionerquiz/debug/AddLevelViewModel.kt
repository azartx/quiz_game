package com.solo4.millionerquiz.debug

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.solo4.millionerquiz.App
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.model.database.PreferredLevelLang
import com.solo4.millionerquiz.model.game.Question
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class AddLevelViewModel : ViewModel(), KoinComponent {

    var targetLevel = ""
    val questions = mutableListOf<Question>()

    fun addLevel() {
        Firebase.firestore
            .collection("levels")
            .document(targetLevel.ifBlank { throw Exception() })
            .set(
                Gson().fromJson(
                    "{'questions':" + Gson().toJson(questions.toList()) + "}",
                    Map::class.java
                )
            )
            .addOnSuccessListener {
                Toast.makeText(get<App>(), "Success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(get<App>(), "Failure", Toast.LENGTH_SHORT).show()
                Log.e("AddLevelViewModel", it.stackTraceToString())
            }
    }

    fun addQuestion(question: Question) {
        questions.add(question)
    }

    fun createLevel(levelNumber: String) {
        targetLevel = levelNumber
    }

    fun addPastedJSON() {
        val json = App.app.resources.openRawResource(R.raw.questions2_ru).bufferedReader().use { it.readText() }
        Firebase.firestore.collection(PreferredLevelLang.ru.levelsDbName)
            .document("2")
            .set(Gson().fromJson(json, Map::class.java))
            .addOnSuccessListener {
                Toast.makeText(App.app, "Success", Toast.LENGTH_SHORT).show()
            }
    }
}
