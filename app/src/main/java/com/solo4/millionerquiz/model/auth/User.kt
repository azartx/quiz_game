package com.solo4.millionerquiz.model.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseUser
import com.solo4.millionerquiz.data.auth.AuthManager.Companion.DEF_USERNAME
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

        var currentLevel: Int
            get() = get<SharedPreferences>(SharedPreferences::class.java)
                .getInt("LAST_LEVEL", 1)
            set(value) {
                get<SharedPreferences>(SharedPreferences::class.java).edit {
                    putInt("LAST_LEVEL", value)
                }
            }
    }
}
