package com.solo4.millionerquiz.data.repositories.score.impl

import com.solo4.millionerquiz.data.database.FirestoreDB
import com.solo4.millionerquiz.data.repositories.score.ScoreRepository
import com.solo4.millionerquiz.model.auth.User
import com.solo4.millionerquiz.model.score.UserScoreItem

class ScoreRepositoryImpl(private val firestoreDB: FirestoreDB) : ScoreRepository {
    override suspend fun getUsersScore(): List<UserScoreItem> {
        return firestoreDB.getAllScoresData()
    }

    override suspend fun updateUserScore(user: User, score: Int) {
        firestoreDB.updateUserScore(user, score)
    }
}