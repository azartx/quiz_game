package com.solo4.millionerquiz.data.auth

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.solo4.millionerquiz.data.Storage
import com.solo4.millionerquiz.model.auth.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthManager(private val storage: Storage) {
    private val firebaseAuth by lazy { Firebase.auth }

    val authState = MutableStateFlow<AuthState>(AuthState.None())

    init {
        CoroutineScope(Dispatchers.Main).launch {
            if (!isAuthenticated()) return@launch
            authState.emit(
                if (isUserAnonymous())
                    AuthState.Anon(User.map(firebaseAuth.currentUser, DEF_USERNAME_ANON)) else
                        AuthState.ByEmail(User.map(firebaseAuth.currentUser))
            )
        }
    }

    fun isAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun isUserAnonymous(): Boolean {
        return firebaseAuth.currentUser?.isAnonymous ?: false
    }

    suspend fun signInAnonymously(username: String = DEF_USERNAME_ANON) = suspendCoroutine { cont ->
        firebaseAuth.signInAnonymously()
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    changeUsername(username)
                    updateAuthState(AuthState.Anon(User.map(it.user)))
                    cont.resume(true)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error while sign in anonymously:", exception)
                cont.resume(false)
            }
    }

    suspend fun createUserByEmail(
        email: String,
        password: String,
        username: String = DEF_USERNAME
    ) = suspendCoroutine { cont ->
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    changeUsername(username)
                    updateAuthState(AuthState.ByEmail(User.map(it.user)))
                    cont.resume(true)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error while create user by email:", exception)
                cont.resume(false)
            }
    }

    suspend fun signInByEmail(email: String, password: String, username: String = DEF_USERNAME) =
        suspendCoroutine { cont ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        changeUsername(username)
                        updateAuthState(AuthState.ByEmail(User.map(it.user)))
                        cont.resume(true)
                    }
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
                    updateAuthState(AuthState.ByEmail(User.map(it.user)))
                }
                ?.addOnFailureListener { exception ->
                    Log.e(TAG, "Error while create user by email from anon user:", exception)
                    cont.resume(false)
                }
                ?: cont.resume(false)
        }

    suspend fun changeUsername(newUsername: String) = suspendCoroutine { cont ->
        firebaseAuth.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(newUsername)
                .build()
        )
            ?.addOnSuccessListener {
                CoroutineScope(Dispatchers.Main).launch {
                    authState.emit(authState.value.updateUserName(newUsername))
                    cont.resume(true)
                }
            }
            ?.addOnFailureListener { exception ->
                Log.e(TAG, "Error while setup new username:", exception)
                cont.resume(false)
            }
    }

    suspend fun logOut() {
        firebaseAuth.signOut()
        authState.emit(AuthState.None())
    }

    fun updateAuthState(newState: AuthState) {
        CoroutineScope(Dispatchers.Main).launch { authState.emit(newState) }
    }

    suspend fun updateUserPhoto(newPhoto: Uri) {
        val photoUrl = storage.uploadPhoto(newPhoto, authState.value.user.name.plus("_${(0..1000).random()}"))
        firebaseAuth.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder().setPhotoUri(photoUrl).build()
        )
        authState.emit(authState.value.updateUserPhoto(photoUrl?.toString() ?: ""))
    }

    companion object {
        const val TAG = "AuthManager"

        const val DEF_USERNAME = "Unnamed User"
        const val DEF_USERNAME_ANON = "Anonymous User"
    }
}
