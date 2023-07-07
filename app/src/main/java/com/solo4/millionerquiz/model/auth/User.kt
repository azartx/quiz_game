package com.solo4.millionerquiz.model.auth

import com.google.firebase.auth.FirebaseUser

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
    }
}
