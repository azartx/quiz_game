package com.solo4.millionerquiz.data.auth

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthManager {
    private val firebaseAuth by lazy { Firebase.auth }

    val authState = MutableStateFlow<AuthState>(AuthState.None)

    init {
        CoroutineScope(Dispatchers.Main).launch {
            if (!isAuthenticated()) return@launch
            authState.emit(
                if (isUserAnonymous()) AuthState.Anon(
                    firebaseAuth.currentUser ?: return@launch
                ) else AuthState.ByEmail(firebaseAuth.currentUser ?: return@launch)
            )
        }
    }

    fun isAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun isUserAnonymous(): Boolean {
        return firebaseAuth.currentUser?.isAnonymous ?: false
    }

    suspend fun signInAnonymously() = suspendCoroutine { cont ->
        firebaseAuth.signInAnonymously()
            .addOnSuccessListener {
                cont.resume(true)
                updateAuthState(AuthState.Anon(it.user!!))
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error while sign in anonymously:", exception)
                cont.resume(false)
            }
    }

    suspend fun createUserByEmail(email: String, password: String) = suspendCoroutine { cont ->
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                cont.resume(true)
                updateAuthState(AuthState.ByEmail(it.user!!))
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error while create user by email:", exception)
                cont.resume(false)
            }
    }

    suspend fun signInByEmail(email: String, password: String) = suspendCoroutine { cont ->
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                cont.resume(true)
                updateAuthState(AuthState.ByEmail(it.user!!))
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error while sign in by email:", exception)
                cont.resume(false)
            }
    }

    suspend fun createUserByEmailFromAnon(email: String, password: String) =
        suspendCoroutine { cont ->
            val credential = EmailAuthProvider.getCredential(email, password)
            firebaseAuth.currentUser?.linkWithCredential(credential)
                ?.addOnSuccessListener {
                    cont.resume(true)
                    updateAuthState(AuthState.ByEmail(it.user!!))
                }
                ?.addOnFailureListener { exception ->
                    Log.e(TAG, "Error while create user by email from anon user:", exception)
                    cont.resume(false)
                }
                ?: cont.resume(false)
        }

    suspend fun logOut() {
        firebaseAuth.signOut()
        authState.emit(AuthState.None)
    }

    fun updateAuthState(newState: AuthState) {
        CoroutineScope(Dispatchers.Main).launch { authState.emit(newState) }
    }

    private companion object {
        const val TAG = "AuthManager"
    }
}
