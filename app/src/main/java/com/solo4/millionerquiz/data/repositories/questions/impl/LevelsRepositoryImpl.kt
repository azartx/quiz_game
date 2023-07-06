package com.solo4.millionerquiz.data.repositories.questions.impl

import com.solo4.millionerquiz.data.database.FirestoreDB
import com.solo4.millionerquiz.data.repositories.questions.LevelsRepository
import com.solo4.millionerquiz.model.game.Level

class LevelsRepositoryImpl(private val firestoreDB: FirestoreDB) : LevelsRepository {
    override suspend fun getAllLevels(): List<Level> {
        return firestoreDB.getAllLevelsData()
    }

    override suspend fun getSpecificLevel(levelNumber: Int): Level {
        return firestoreDB.getLevelData(levelNumber)
    }

    override suspend fun getLevelsCount(): Int {
        return firestoreDB.getLevelsCount()
    }
}