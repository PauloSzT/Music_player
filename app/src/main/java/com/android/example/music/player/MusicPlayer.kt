package com.android.example.music.player

import android.media.MediaPlayer
import com.android.example.music.models.Song

interface MusicPlayer {

    fun playSong(songIndex: Int)
    fun pauseCurrentSong()
    fun resumeCurrentSong()
    fun stopSong()
    fun skipPrev()
    fun skipNext()
}

class MusicPlayerImplementation(
    private val songsList: List<Song>
) : MusicPlayer {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var currentSongIndex: Int = 0

    override fun playSong(songIndex: Int) {
        mediaPlayer.setDataSource(songsList[songIndex].path)
        mediaPlayer.prepare()
        mediaPlayer.setVolume(1.0f, 1.0f)
        mediaPlayer.start()
        currentSongIndex = songIndex
    }

    override fun pauseCurrentSong() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun resumeCurrentSong() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    override fun stopSong() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
    }

    override fun skipPrev() {

        mediaPlayer.stop()
        if (currentSongIndex == 0) {
            playSong(songsList.size - 1)
        } else {
            playSong(currentSongIndex - 1)
        }
    }

    override fun skipNext() {
        mediaPlayer.stop()
        if (currentSongIndex == songsList.size - 1) {
            playSong(0)
        } else {
            playSong(currentSongIndex + 1)
        }
    }
}
