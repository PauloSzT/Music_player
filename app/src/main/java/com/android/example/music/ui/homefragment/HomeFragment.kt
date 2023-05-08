package com.android.example.music.ui.homefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColorStateList
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.example.music.MainActivityViewModel
import com.android.example.music.R
import com.android.example.music.databinding.FragmentHomeBinding
import com.android.example.music.player.MusicPlayerImplementation

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.fabSettings.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val songsObserver = Observer<MusicPlayerImplementation?> { musicPlayer ->
            musicPlayer?.let { player ->
                val adapter =
                    SongListAdapter(data = player.playList, requireContext()) { indexSong ->
                        player.playSong(indexSong)
                        activityViewModel.musicPlayer.value?.let{ player ->
                            navigateToPlayFragment(
                                songName = player.playList[indexSong].name
                            )
                        }
//                            val intent = Intent("android.intent.action.ACTION_PLAY")
//                            intent.putExtra("song_title", "My Song Title")
//                            intent.putExtra("song_artist", "My Song Artist")
//                            intent.putExtra("song_duration", 180) // song duration in seconds
//                            context?.sendBroadcast(intent)
                    }
                binding.songContainer.layoutManager = LinearLayoutManager(requireContext())
                binding.songContainer.adapter = adapter
                binding.fabPlayList.setOnClickListener {
                    val currentPlayingIndex = player.playList()
                    activityViewModel.musicPlayer.value?.let{ player ->
                        navigateToPlayFragment(
                            songName = player.playList[currentPlayingIndex].name
                        )
                    }
                }
                val shuffleObserver = Observer<Boolean> { isShuffling ->
                    if (isShuffling) {
                        binding.fabShuffle.backgroundTintList =
                            getColorStateList(requireContext(), R.color.pink)
                    } else {
                        binding.fabShuffle.backgroundTintList =
                            getColorStateList(requireContext(), R.color.white)
                    }
                }
                binding.fabShuffle.setOnClickListener {
                    player.toggleShuffle()
                }
                player.isShuffle.observe(requireActivity(), shuffleObserver)
            }
        }
        activityViewModel.musicPlayer.observe(
            requireActivity(),
            songsObserver
        )
    }

    private fun navigateToPlayFragment(songName: String) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToPlayFragment(songName)
        findNavController().navigate(action)
    }
}
