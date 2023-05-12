package com.android.example.music.models

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.android.example.music.R

class RawFilesProvider : ContentProvider() {

    private var uriList = arrayListOf(
        Uri.parse("$URI_RSC${R.raw.charlottedewittetheageoflove}"),
        Uri.parse("$URI_RSC${R.raw.deadmau5strobe}"),
        Uri.parse("$URI_RSC${R.raw.dimitrivegasmamoth}"),
        Uri.parse("$URI_RSC${R.raw.kaiserdiscoaguja}"),
        Uri.parse("$URI_RSC${R.raw.kennybrianaleluya}"),
        Uri.parse("$URI_RSC${R.raw.kennybriancandela}"),
        Uri.parse("$URI_RSC${R.raw.kennybrianjaleo}"),
        Uri.parse("$URI_RSC${R.raw.kennybriannuestroamigo}"),
        Uri.parse("$URI_RSC${R.raw.lostfrequenciessickofthesilence}"),
        Uri.parse("$URI_RSC${R.raw.xpress2muzikxpress}"),
    )

    companion object {
        private const val PROVIDER_NAME = "com.example.music.models/RawFilesProvider"
        private const val PLAYLIST_PATH = "uriList"
        val CONTENT_URI: Uri = Uri.parse("content://$PROVIDER_NAME/$PLAYLIST_PATH")
        private const val PACKAGE_NAME = "com.android.example.music"
        const val URI_RSC = "android.resource://$PACKAGE_NAME/"
        const val URI = "URI_PATH"
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val cursor = MatrixCursor(arrayOf(URI))
        uriList.forEach {
            cursor.addRow(arrayOf(it))
        }
        return cursor
    }

    override fun getType(p0: Uri): String? {
        throw UnsupportedOperationException("Not supported")
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        throw UnsupportedOperationException("Not supported")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        throw UnsupportedOperationException("Not supported")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        throw UnsupportedOperationException("Not supported")
    }
}
