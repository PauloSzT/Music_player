package com.android.example.music

import android.app.Application
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.music.broadcast.MusicBroadcastReceiver
import com.android.example.music.models.Song
import com.android.example.music.models.SongProvider
import com.android.example.music.player.MusicPlayerImplementation

class MainActivityViewModel(private val application: Application) : ViewModel() {

    val musicPlayer = MutableLiveData<MusicPlayerImplementation?>(null)
    private val songProvider: SongProvider = SongProvider(application.contentResolver)

    fun initializePlayer() {
        var list = songProvider.getSongsList().mapIndexed { index, item ->
                val metadataRetriever = MediaMetadataRetriever()
                val uri = Uri.parse(item )
                metadataRetriever.setDataSource(application, uri)
                val trackName =
                    metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                        .toString()
                 Song(
                    path = item,
                    index = index,
                    name = trackName,
                    isInPlaylist = MutableLiveData((0..2).contains(index))
                )
            }
        musicPlayer.value = MusicPlayerImplementation(list, viewModelScope,::sendBroadcast, context = application)
    }
    private fun sendBroadcast(songName:String){
        val intent = Intent(application, MusicBroadcastReceiver::class.java)
        intent.action = BROADCAST_ACTION
        intent.putExtra("songName",songName)
        application.sendBroadcast(intent)
    }

    companion object {
        const val BROADCAST_ACTION = "com.example.music.MUSIC_BROADCAST"
    }
}
