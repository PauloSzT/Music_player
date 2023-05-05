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
                    SongListAdapter(data = player.songsList, requireContext()) { indexSong ->
                        player.playSong(indexSong)
                        navigateToPlayFragment()
                    }
                binding.songContainer.layoutManager = LinearLayoutManager(requireContext())
                binding.songContainer.adapter = adapter
                binding.fabPlayList.setOnClickListener {
                    player.playList()
                    navigateToPlayFragment()
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

    private fun navigateToPlayFragment() {
        val action =
            HomeFragmentDirections.actionHomeFragmentToPlayFragment()
        findNavController().navigate(action)
    }
}

