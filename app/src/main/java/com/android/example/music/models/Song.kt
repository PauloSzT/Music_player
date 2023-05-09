package com.android.example.music.models

import androidx.lifecycle.MutableLiveData

class Song(
    val path: String,
    var index: Int,
    val name: String,
    var isInPlaylist: MutableLiveData<Boolean>
)
