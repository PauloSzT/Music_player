package com.android.example.music.player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.android.example.music.models.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface MusicPlayer {

    fun playSong(songIndex: Int)
    fun pauseOrResumeCurrentSong(): Boolean
    fun skipPrev() : String
    fun skipNext() : String
    fun toggleShuffle()
    fun playList(): Int
    fun seekTo(position: Int)
    fun addSong(songIndex: Int)
    fun removeSong(songIndex: Int)
    fun destroyPlayer()
}

class MusicPlayerImplementation(
    var songsList: List<Song>,
    private val viewModelScope: CoroutineScope,
    val sendBroadcastCallback: (String) -> Unit,
    val context: Context
) : MusicPlayer {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var currentSongIndex: Int = 0
    val isShuffle = MutableLiveData(false)
    val seekBarPosition = MutableLiveData<Pair<Int, Int>>()
    private var isSeekBarRunning = false
    var playList: List<Song> = songsList.filter{
        it.isInPlaylist.value == true
    }

    override fun playSong(songIndex: Int) {
        destroyPlayer()
        mediaPlayer.setDataSource(context,Uri.parse(playList[songIndex].path))
        mediaPlayer.prepare()
        mediaPlayer.setVolume(1.0f, 1.0f)
        mediaPlayer.start()
        currentSongIndex = songIndex
        sendBroadcastCallback(playList[currentSongIndex].name)
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

    override fun skipPrev() : String{
        if (currentSongIndex == 0) {
            playSong(playList.size - 1)
            return playList[playList.size - 1].name
        } else {
            playSong(currentSongIndex - 1)
            return playList[currentSongIndex].name
        }
    }

    override fun skipNext() : String {
        if (isShuffle.value == true) {
            val random = (playList.indices).random()
            playSong(random)
            return playList[random].name
        } else {
            if (currentSongIndex == playList.size - 1) {
                playSong(0)
                return playList[0].name
            } else {
                playSong(currentSongIndex + 1)
                return playList[currentSongIndex].name
            }
        }
    }

    override fun playList() : Int {
        if (isShuffle.value == true) {
            val random = (playList.indices).random()
            playSong(random)
            mediaPlayer.setOnCompletionListener {
                val randomNext = (playList.indices).random()
                playSong(randomNext)
            }
            return random
        } else {
            playSong(0)
            mediaPlayer.setOnCompletionListener { playSong(currentSongIndex + 1) }
            return 0
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
        playList = songsList.filter{
            it.isInPlaylist.value == true
        }.mapIndexed { index, song ->
            song.index = index
            song
        }
    }

    override fun removeSong(songIndex: Int) {
        songsList[songIndex].isInPlaylist.value = false
        playList = songsList.filter{
            it.isInPlaylist.value == false
        }.mapIndexed { index, song ->
            song.index = index
            song
        }
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
