package com.solo4.millionerquiz.data

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.SoundPool
import android.net.Uri
import androidx.core.content.edit
import com.solo4.millionerquiz.App
import com.solo4.millionerquiz.R
import org.koin.java.KoinJavaComponent
import java.io.ByteArrayOutputStream
import java.io.File

class MediaManager(private val sharedPreferences: SharedPreferences) {

    var isMusicEnabled: Boolean
        get() = sharedPreferences.getBoolean(PREFS_IS_MUSIC_ENABLED, true)
        set(value) {
            _isMusicEnabled = value
            sharedPreferences.edit { putBoolean(PREFS_IS_MUSIC_ENABLED, value) }
        }

    private var _isMusicEnabled = isMusicEnabled

    private val soundsPool = SoundPool.Builder().setAudioAttributes(
        AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_GAME)
            .build()
    ).build()

    private val poolMusicIds = mutableMapOf<String, Int>()

    init {
        poolMusicIds[POOL_MUSIC_CLICK] =
            soundsPool.load(App.app.resources.openRawResourceFd(R.raw.click), 1)
    }

    fun prepareMultipartData(
        context: Context,
        imageUri: Uri,
        maxFileSizeMb: Float = 2.0f
    ): ByteArray {
        val mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"
        val file = context.contentResolver.openInputStream(imageUri).use { stream ->
            val file = File.createTempFile(
                getNameFromUri(imageUri),
                getExtensionFromMime(mimeType)
            )
            file.outputStream().use { stream?.copyTo(it) }
            file
        }
        return compressImage(file, maxFileSizeMb)
    }

    fun playClickSound() {
        if (_isMusicEnabled) {
            soundsPool.play(
                poolMusicIds[POOL_MUSIC_CLICK]!!,
                1f,
                1f,
                1,
                0,
                1f
            )
        }
    }

    private fun compressImage(file: File, maxFileSizeMb: Float): ByteArray {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.path, options)
        var quality = 100
        var compressedFile: File
        var bytes: ByteArray
        do {
            quality -= 10
            val outputStream = ByteArrayOutputStream()
            val bitmap = BitmapFactory.decodeFile(file.path)
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            compressedFile = File.createTempFile("user_photo", ".jpg")
            bytes = outputStream.toByteArray()
        } while (compressedFile.length() > maxFileSizeMb * 1024 * 1024)
        return bytes
    }

    private fun getNameFromUri(uri: Uri, default: String = "File"): String {
        return uri.path?.substringAfterLast("/")?.substringBefore(".") ?: default
    }

    private fun getExtensionFromMime(mime: String): String {
        return when (mime) {
            "plain/text" -> ".txt"
            "application/pdf" -> ".pdf"
            "application/msword" -> ".doc"
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> ".docx"
            else -> ".unknown"
        }
    }

    companion object {
        const val PREFS_IS_MUSIC_ENABLED = "PREFS_IS_MUSIC_ENABLED"

        const val POOL_MUSIC_CLICK = "POOL_MUSIC_CLICK"

        fun playClick() {
            KoinJavaComponent.get<MediaManager>(MediaManager::class.java).playClickSound()
        }
    }
}
