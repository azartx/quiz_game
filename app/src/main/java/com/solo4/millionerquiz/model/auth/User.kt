package com.solo4.millionerquiz.model.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseUser
import org.koin.java.KoinJavaComponent.get

data class User(
    val name: String,
    val profileImageUrl: String,
    val isAnonymous: Boolean,
    val email: String? = null
) {
    companion object {
        private const val IMAGE_PLACEHOLDER =
            "https://cdn4.iconfinder.com/data/icons/political-elections/50/48-1024.png"

        val unknown: User = User(
            name = "Anonymous User",
            profileImageUrl = IMAGE_PLACEHOLDER,
            isAnonymous = true
        )

        fun map(firebaseUser: FirebaseUser?): User {
            return User(
                name = firebaseUser?.displayName ?: "Unknown Username",
                profileImageUrl = firebaseUser?.photoUrl?.toString() ?: IMAGE_PLACEHOLDER,
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
