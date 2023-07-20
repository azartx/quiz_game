package com.solo4.millionerquiz.data.auth

import androidx.annotation.Keep
import com.solo4.millionerquiz.model.auth.User

@Keep
sealed class AuthState {
    abstract val user: User
    data class None(override val user: User = User.unknown) : AuthState()

    data class Anon(override val user: User) : AuthState()
    data class ByEmail(override val user: User) : AuthState()

    fun updateUserPhoto(newPhotoUrl: String): AuthState {
        val _user = user.copy(profileImageUrl = newPhotoUrl)

        return when (this) {
            is None -> None(_user)
            is Anon -> Anon(_user)
            is ByEmail -> ByEmail(_user)
        }
    }

    fun updateUserName(newUsername: String): AuthState {
        val _user = user.copy(name = newUsername)

        return when (this) {
            is None -> None(_user)
            is Anon -> Anon(_user)
            is ByEmail -> ByEmail(_user)
        }
    }
}
