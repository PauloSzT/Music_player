package com.android.example.music.models

import android.content.ContentResolver
import android.os.Build
import android.provider.MediaStore
import android.net.Uri

class SongProvider(private val contentResolver: ContentResolver) {

    private val newSongsList = mutableListOf<String>()
    private val collection: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }
    private val projection = arrayOf(
        MediaStore.Audio.Media.RELATIVE_PATH,
        MediaStore.Audio.Media.DISPLAY_NAME
    )
    private val selection = "${MediaStore.Audio.Media.RELATIVE_PATH} = ?"
    private val selectionArgs = arrayOf("Music/Music-App/")

    fun getSongsList(): List<String> {
        val query = contentResolver.query(collection, projection, selection, selectionArgs, null)
        query?.use { cursor ->
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.RELATIVE_PATH)
            val displayColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                newSongsList.add("${cursor.getString(pathColumn)}${cursor.getString(displayColumn)}")
                cursor.moveToNext()
            }
        }
        return newSongsList
    }
}
