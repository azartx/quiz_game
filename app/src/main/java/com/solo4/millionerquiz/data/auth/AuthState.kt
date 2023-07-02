package com.solo4.millionerquiz.data.auth

import com.google.firebase.auth.FirebaseUser

sealed class AuthState {
    object None : AuthState()
    data class Anon(override val currentUser: FirebaseUser) : AuthState(), Authenticated
    data class ByEmail(override val currentUser: FirebaseUser) : AuthState(), Authenticated
}

interface Authenticated {
    val currentUser: FirebaseUser
}
