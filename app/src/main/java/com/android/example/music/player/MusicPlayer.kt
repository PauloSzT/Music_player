package com.android.example.music.player

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import com.android.example.music.models.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface MusicPlayer {

    fun playSong(songIndex: Int)
    fun pauseOrResumeCurrentSong(): Boolean
    fun skipPrev()
    fun skipNext()
    fun toggleShuffle()
    fun playList()
    fun seekTo(position: Int)
    fun addSong(songIndex: Int)
    fun removeSong(songIndex: Int)
    fun destroyPlayer()
}

class MusicPlayerImplementation(
    var songsList: List<Song>,
    private val viewModelScope: CoroutineScope
) : MusicPlayer {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var currentSongIndex: Int = 0
    val isShuffle = MutableLiveData(false)
    val seekBarPosition = MutableLiveData<Pair<Int, Int>>()
    private var isSeekBarRunning = false


    override fun playSong(songIndex: Int) {
        mediaPlayer.setDataSource(songsList[songIndex].path)
        mediaPlayer.prepare()
        mediaPlayer.setVolume(1.0f, 1.0f)
        mediaPlayer.start()
        currentSongIndex = songIndex
        initializeSeekBar()
    }

    override fun pauseOrResumeCurrentSong(): Boolean {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            return true
        } else {
            mediaPlayer.start()
            return false
        }
    }

    override fun skipPrev() {
        if (currentSongIndex == 0) {
            destroyPlayer()
            playSong(songsList.size - 1)
        } else {
            destroyPlayer()
            playSong(currentSongIndex - 1)
        }
    }

    override fun skipNext() {
        if (isShuffle.value == true) {
            val random = (songsList.indices).random()
            destroyPlayer()
            playSong(random)
        } else {
            if (currentSongIndex == songsList.size - 1) {
                destroyPlayer()
                playSong(0)
            } else {
                destroyPlayer()
                playSong(currentSongIndex + 1)
            }
        }
    }

    override fun playList() {
        if (isShuffle.value == true) {
            val random = (songsList.indices).random()
            playSong(random)
            mediaPlayer.setOnCompletionListener {
                val randomNext = (songsList.indices).random()
                playSong(randomNext)
            }
        } else {
            playSong(0)
            mediaPlayer.setOnCompletionListener { playSong(currentSongIndex + 1) }
        }
    }

    override fun seekTo(position: Int) {
        mediaPlayer.seekTo(position)
    }

    override fun toggleShuffle() {
        isShuffle.value?.let {
            isShuffle.value = !it
        }
    }

    override fun addSong(songIndex: Int) {
        songsList[songIndex].isInPlaylist.value = true
    }

    override fun removeSong(songIndex: Int) {
        songsList[songIndex].isInPlaylist.value = false
    }

    override fun destroyPlayer() {
        mediaPlayer.stop()
        mediaPlayer.reset()
    }

    private fun initializeSeekBar() {
        viewModelScope.launch {
            isSeekBarRunning = true
            while (isSeekBarRunning) {
                seekBarPosition.value = Pair(mediaPlayer.currentPosition, mediaPlayer.duration)
                delay(1000)
            }
        }
    }
}
