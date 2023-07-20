package com.solo4.millionerquiz.model.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseUser
import com.solo4.millionerquiz.data.auth.AuthManager.Companion.DEF_USERNAME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.java.KoinJavaComponent.get
import java.util.UUID

data class User(
    val id: String,
    val name: String,
    val profileImageUrl: String?,
    val isAnonymous: Boolean,
    val email: String? = null
) {
    companion object {
        private const val IMAGE_PLACEHOLDER =
            "https://cdn4.iconfinder.com/data/icons/political-elections/50/48-1024.png"

        val unknown: User = User(
            id = UUID.randomUUID().toString(),
            name = DEF_USERNAME,
            profileImageUrl = null,
            isAnonymous = true
        )

        fun map(firebaseUser: FirebaseUser?, backingUsername: String = DEF_USERNAME): User {
            return User(
                id = firebaseUser?.uid ?: UUID.randomUUID().toString(),
                name = firebaseUser?.displayName ?: backingUsername,
                profileImageUrl = firebaseUser?.photoUrl?.toString(),
                isAnonymous = firebaseUser?.isAnonymous ?: true,
                email = firebaseUser?.email
            )
        }

        private const val LAST_LEVEL_KEY = "LAST_LEVEL"

        suspend fun updateLastCompletedLevel(lastCompletedLevel: Int) {
            get<SharedPreferences>(SharedPreferences::class.java).apply {
                edit { putInt(LAST_LEVEL_KEY, lastCompletedLevel) }
                _lastCompletedLevel.emit(lastCompletedLevel)
            }
        }

        private val _lastCompletedLevel = MutableStateFlow(
            get<SharedPreferences>(SharedPreferences::class.java)
                .getInt(LAST_LEVEL_KEY, 1)
        )
        val lastCompletedLevel = _lastCompletedLevel.asStateFlow()
    }
}
