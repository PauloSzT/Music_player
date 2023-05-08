package com.android.example.music

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.android.example.music.models.Song

class MainActivity : AppCompatActivity() {
    private lateinit var viewModelFactory: MainActivityViewModelFactory
    private lateinit var viewModel: MainActivityViewModel
    private var requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                isGranted: Boolean ->
            if (isGranted) {
                viewModel.initializePlayer()
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newSongsList = mutableListOf<Song>()
        val collection = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        }else{
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.RELATIVE_PATH,
            MediaStore.Audio.Media.DISPLAY_NAME
        )
        val selection = ""
        val selectionArgs = arrayOf("")
        val query = contentResolver.query(collection,projection,selection,selectionArgs,"")
        query?.use {cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            Log.wtf("paulocode", "$idColumn")
        }


        setContentView(R.layout.activity_main)
        REQUIRED_PERMISSIONS.forEach {
            requestPermissionLauncher.launch(it)
        }
        viewModelFactory = MainActivityViewModelFactory(application)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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

    companion object {
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
