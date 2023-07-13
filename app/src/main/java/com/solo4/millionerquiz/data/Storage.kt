package com.solo4.millionerquiz.data

import android.net.Uri
import android.os.SystemClock
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.solo4.millionerquiz.App
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Storage(private  val mediaManager: MediaManager) {
    private val firebaseStorage = Firebase.storage

    suspend fun uploadPhoto(uri: Uri, userMark: String = SystemClock.elapsedRealtime().toString()) =
        suspendCoroutine<Uri?> { cont ->
            val imageBytes = mediaManager.prepareMultipartData(App.app, uri)
            firebaseStorage.reference
                .child("$REF_USER_PHOTOS/user_image_$userMark.jpg")
                .putBytes(imageBytes)
                .addOnSuccessListener {
                    firebaseStorage.reference
                        .child("$REF_USER_PHOTOS/user_image_$userMark.jpg")
                        .downloadUrl
                        .addOnSuccessListener {
                            cont.resume(it)
                        }
                        .addOnFailureListener {
                            Log.e("Storage", "Error while uploading user photo", it)
                            cont.resume(null)
                        }
                }
                .addOnFailureListener {
                    Log.e("Storage", "Error while uploading user photo", it)
                    cont.resume(null)
                }

        }

    companion object {
        const val REF_USER_PHOTOS = "user_photos"
    }
}
