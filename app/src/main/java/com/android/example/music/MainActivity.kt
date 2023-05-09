package com.android.example.music

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.android.example.music.broadcast.MusicBroadcastReceiver
import com.android.example.music.models.SongProvider

class MainActivity : AppCompatActivity() {
    private lateinit var viewModelFactory: MainActivityViewModelFactory
    private lateinit var viewModel: MainActivityViewModel
    private var requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.initializePlayer()
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeBroadcastReceiver()
        REQUIRED_PERMISSIONS.forEach {
            requestPermissionLauncher.launch(it)
        }
        SongProvider(contentResolver)
        viewModelFactory = MainActivityViewModelFactory(application)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.initializePlayer()
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

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun initializeBroadcastReceiver() {
        val filter = IntentFilter(BROADCAST_ACTION)
        this.registerReceiver(MusicBroadcastReceiver(), filter)
    }

    companion object {
        private const val BROADCAST_ACTION = "com.example.music.MUSIC_BROADCAST"
        private const val REQUEST_CODE_PERMISSIONS = 100
        private val REQUIRED_PERMISSIONS =
            mutableListOf<String>().apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    add(Manifest.permission.READ_MEDIA_AUDIO)
                }
            }.toTypedArray()
    }
}
