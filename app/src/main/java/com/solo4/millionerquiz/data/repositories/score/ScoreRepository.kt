package com.solo4.millionerquiz.data.repositories.score

import com.solo4.millionerquiz.model.auth.User
import com.solo4.millionerquiz.model.score.UserScoreItem

interface ScoreRepository {
    suspend fun getUsersScore(): List<UserScoreItem>
    suspend fun updateUserScore(user: User, score: Int)
}
