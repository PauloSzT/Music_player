package com.android.example.music

import android.app.Application
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.music.models.Song
import com.android.example.music.player.MusicPlayerImplementation
import java.io.File

class MainActivityViewModel(private val application: Application) : ViewModel() {

    val musicPlayer = MutableLiveData<MusicPlayerImplementation?>(null)
    private val folder = Environment.getExternalStorageDirectory()
    fun initializePlayer() {
        val list =
         File(folder, CHILD_ROUTE)
            .listFiles()?.mapIndexed { index, item ->
                val metadataRetriever = MediaMetadataRetriever()
                val uri = Uri.parse(item.path)
                metadataRetriever.setDataSource(application, uri)
                val trackName =
                    metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                        .toString()
                Song(
                    path = item.path,
                    index = index,
                    name = trackName,
                    isInPlaylist = (0..2).contains(index)
                )
            } ?: emptyList()
        musicPlayer.value = MusicPlayerImplementation(list)
    }

    companion object {
        const val CHILD_ROUTE = "Music/Music-App"
    }
}
