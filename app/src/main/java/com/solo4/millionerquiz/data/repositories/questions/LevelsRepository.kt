package com.solo4.millionerquiz.data.repositories.questions

import com.solo4.millionerquiz.model.game.Level

interface LevelsRepository {
    suspend fun getAllLevels(): List<Level>
    suspend fun getSpecificLevel(levelNumber: Int): Level
    suspend fun getLevelsCount(): Int
}
