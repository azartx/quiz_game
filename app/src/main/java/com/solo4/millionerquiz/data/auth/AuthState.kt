package com.solo4.millionerquiz.data.auth

import com.solo4.millionerquiz.model.auth.User

sealed class AuthState {
    abstract val user: User
    object None : AuthState() {
        override val user: User = User.unknown
    }

    data class Anon(override val user: User = User.unknown) : AuthState()
    data class ByEmail(override val user: User) : AuthState()
}
