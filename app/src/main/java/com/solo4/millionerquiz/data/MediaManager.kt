package com.solo4.millionerquiz.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File

class MediaManager {
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
            /*val fileOutputStream = FileOutputStream(compressedFile)
            fileOutputStream.write(outputStream.toByteArray())
            fileOutputStream.flush()
            fileOutputStream.close()*/
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
}
