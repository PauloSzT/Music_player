package com.android.example.music.ui.settingsfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.example.music.MainActivityViewModel
import com.android.example.music.databinding.FragmentSettingsBinding
import com.android.example.music.player.MusicPlayerImplementation

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val songsObserver = Observer<MusicPlayerImplementation?> { musicPlayer ->
            musicPlayer?.let { player ->
                val adapter =
                    SettingsListAdapter(
                        data = player.songsList, requireContext(), requireActivity()
                    ) { indexSong ->
                        if (player.songsList[indexSong].isInPlaylist.value == true) {
                            player.removeSong(indexSong)
                        } else {
                            player.addSong(indexSong)
                        }
                    }
                binding.songContainer.layoutManager = LinearLayoutManager(requireContext())
                binding.songContainer.adapter = adapter
            }
        }
        activityViewModel.musicPlayer.observe(
            requireActivity(),
            songsObserver
        )
    }
}
