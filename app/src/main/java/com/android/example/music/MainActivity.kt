package com.android.example.music

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.android.example.music.models.Song
import com.android.example.music.player.MusicPlayer
import com.android.example.music.player.MusicPlayerImplementation
import java.io.File
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var musicPlayer: MusicPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (allPermissionsGranted()) {
            Log.wtf("paulocode","all permissions" )
            initializePlayer()
        } else {
            Log.wtf("paulocode","trying to get permissions1" )
            ActivityCompat.requestPermissions(this@MainActivity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
            Log.wtf("paulocode","trying to get permissions2" )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            applicationContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                initializePlayer()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                this@MainActivity.finish()
            }
        }
    }

    fun initializePlayer(){
        val folder = Environment.getExternalStorageDirectory()
        val songsList = File(folder, CHILD_ROUTE)
            .listFiles()?.mapIndexed { index, item ->
                Song(name = item.path, url = index)
            }
        //            musicPlayer = MusicPlayerImplementation(songsList)
    }




    companion object {
        private const val CHILD_ROUTE = "Music/Music-App"
        private const val TAG = "MusicApp"
        private const val REQUEST_CODE_PERMISSIONS = 100
        private val REQUIRED_PERMISSIONS =
            mutableListOf<String>().apply {
                if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    add(Manifest.permission.READ_MEDIA_AUDIO)
                }
            }.toTypedArray()
    }
}
