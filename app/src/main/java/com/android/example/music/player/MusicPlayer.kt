package com.android.example.music.player

import com.android.example.music.models.Song

interface MusicPlayer {
    fun playCurrentSong()
    fun pauseCurrentSong()
    fun stopSong()
    fun skipPrev()
    fun skipNext()
}

abstract class MusicPlayerImplementation(
    val songsList: List<Song>
) : MusicPlayer {
    override fun pauseCurrentSong() {
        TODO("Not yet implemented")
    }

    override fun stopSong() {
        TODO("Not yet implemented")
    }

    override fun playCurrentSong() {
        TODO("Not yet implemented")
    }

    override fun skipPrev() {
        TODO("Not yet implemented")
    }

    override fun skipNext() {
        TODO("Not yet implemented")
    }
}
