package com.android.example.music.player

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import com.android.example.music.models.Song

interface MusicPlayer {
    fun playCurrentSong()
    fun playSong(songIndex: Int)
    fun pauseCurrentSong()
    fun stopSong()
    fun skipPrev()
    fun skipNext()
}

class MusicPlayerImplementation(
    private val songsList: List<Song>
) : MusicPlayer {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    override fun playCurrentSong() {
        TODO("Not yet implemented")
    }

    override fun playSong(songIndex: Int) {
        mediaPlayer.setDataSource(songsList[songIndex].path)
        mediaPlayer.prepare()
        mediaPlayer.setVolume(1.0f, 1.0f)
        mediaPlayer.start()
    }

    override fun pauseCurrentSong() {
        TODO("Not yet implemented")
    }

    override fun stopSong() {
        TODO("Not yet implemented")
    }

    override fun skipPrev() {
        TODO("Not yet implemented")
    }

    override fun skipNext() {
        TODO("Not yet implemented")
    }
}
