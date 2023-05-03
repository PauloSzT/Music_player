package com.android.example.music

import android.app.Application
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import com.android.example.music.models.Song
import com.android.example.music.player.MusicPlayer
import com.android.example.music.player.MusicPlayerImplementation
import java.io.File

class MainActivityViewModel(private val application: Application) : ViewModel() {

    private lateinit var musicPlayer: MusicPlayer
    fun initializePlayer() {
        val folder = Environment.getExternalStorageDirectory()
        val songsList = File(folder, CHILD_ROUTE)
            .listFiles()?.mapIndexed { index, item ->
                val metadataRetriever = MediaMetadataRetriever()
                val uri = Uri.parse(item.path)
                metadataRetriever.setDataSource(application, uri)
                val trackName =
                    metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                        .toString()
                Song(path = item.path, index = index, name = trackName)
            }
        songsList?.let { list ->
            musicPlayer = MusicPlayerImplementation(list)
            musicPlayer.playSong(0)
        }
    }

    companion object {
        private const val CHILD_ROUTE = "Music/Music-App"
    }
}
