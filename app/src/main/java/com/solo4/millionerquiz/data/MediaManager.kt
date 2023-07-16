package com.solo4.millionerquiz.data

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri
import androidx.core.content.edit
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.solo4.millionerquiz.App
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.data.analytics.Logger
import org.koin.java.KoinJavaComponent
import java.io.ByteArrayOutputStream
import java.io.File

class MediaManager(private val sharedPreferences: SharedPreferences): DefaultLifecycleObserver {

    var isMusicLaunched: Boolean = false

    var isMusicEnabled: Boolean
        get() = sharedPreferences.getBoolean(PREFS_IS_MUSIC_ENABLED, true)
        set(value) {
            _isMusicEnabled = value
            sharedPreferences.edit { putBoolean(PREFS_IS_MUSIC_ENABLED, value) }
        }

    private var _isMusicEnabled = isMusicEnabled
        set(value) {
            field = value
            if (value) {
                playGameMusic()
            } else {
                musicPlayer.pause()
            }
        }

    var isSoundsEnabled: Boolean
        get() = sharedPreferences.getBoolean(PREFS_IS_SOUNDS_ENABLED, true)
        set(value) {
            _isSoundsEnabled = value
            sharedPreferences.edit { putBoolean(PREFS_IS_SOUNDS_ENABLED, value) }
        }

    private var _isSoundsEnabled = isMusicEnabled

    private val soundsPool = SoundPool.Builder().setAudioAttributes(
        AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_GAME)
            .build()
    ).build()

    private val musicPlayer by lazy { MediaPlayer() }

    private val poolMusicIds = mutableMapOf<String, Int>()

    init {
        poolMusicIds[POOL_MUSIC_CLICK] =
            soundsPool.load(App.app.resources.openRawResourceFd(R.raw.click), 1)
        poolMusicIds[POOL_MUSIC_GAME_MUSIC] = R.raw.music2
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
        if (_isSoundsEnabled) {
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

    fun playGameMusic() {
        if (isMusicLaunched) {
            try {
                musicPlayer.start()
            } catch (e: Exception) {
                Logger.e("MediaManager", "Failed resuming music", tr = e)
            }
            return
        }
        if (_isMusicEnabled && !musicPlayer.isPlaying) {
            musicPlayer.apply {
                try {
                    val musicDescriptor = App.app.resources.openRawResourceFd(poolMusicIds[POOL_MUSIC_GAME_MUSIC]!!)

                    setDataSource(musicDescriptor)
                    prepareAsync()
                    setOnPreparedListener {
                        isLooping = true
                        start()
                        isMusicLaunched = true
                    }
                } catch (e: Exception) {
                    Logger.e("MediaManager", "Error while playing app music", tr = e)
                }
            }
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

    override fun onPause(owner: LifecycleOwner) {
        musicPlayer.pause()
        super.onPause(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        playMusic()
    }

    companion object {
        const val PREFS_IS_SOUNDS_ENABLED = "PREFS_IS_SOUNDS_ENABLED"
        const val PREFS_IS_MUSIC_ENABLED = "PREFS_IS_MUSIC_ENABLED"

        const val POOL_MUSIC_CLICK = "POOL_MUSIC_CLICK"
        const val POOL_MUSIC_GAME_MUSIC = "POOL_MUSIC_GAME_MUSIC"

        fun playClick() {
            KoinJavaComponent.get<MediaManager>(MediaManager::class.java).playClickSound()
        }

        fun playMusic() {
            KoinJavaComponent.get<MediaManager>(MediaManager::class.java).playGameMusic()
        }
    }
}
