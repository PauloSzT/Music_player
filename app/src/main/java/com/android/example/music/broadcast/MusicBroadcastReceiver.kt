package com.android.example.music.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MusicBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if(action == BROADCAST_ACTION){
            val songName = intent.getStringExtra("songName")
            Toast.makeText(context,"Now playing $songName",Toast.LENGTH_SHORT).show()
        }
    }
    companion object{
        const val BROADCAST_ACTION = "com.example.music.MUSIC_BROADCAST"
    }
}
