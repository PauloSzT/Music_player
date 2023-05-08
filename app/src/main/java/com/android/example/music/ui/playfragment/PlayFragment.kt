package com.android.example.music.ui.playfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.android.example.music.MainActivityViewModel
import com.android.example.music.R
import com.android.example.music.databinding.FragmentPlayBinding


class PlayFragment : Fragment() {
    private lateinit var binding: FragmentPlayBinding
    private val activityViewModel: MainActivityViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val seekBarObserver = Observer<Pair<Int, Int>> { pair ->
            binding.seekbar.progress = pair.first
            binding.seekbar.max = pair.second
        }
        activityViewModel.musicPlayer.value?.seekBarPosition?.observe(
            requireActivity(),
            seekBarObserver
        )
        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) activityViewModel.musicPlayer.value?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

        binding.fabPlay.setOnClickListener {
            if (activityViewModel.musicPlayer.value?.pauseOrResumeCurrentSong() == true) {
                binding.fabPlay.setImageResource(R.drawable.ic_play_arrow)
            } else {
                binding.fabPlay.setImageResource(R.drawable.ic_pause_circle)
            }
        }
        binding.songName.text

        binding.fabBackward.setOnClickListener {
            activityViewModel.musicPlayer.value?.skipPrev()
        }
        binding.fabForward.setOnClickListener {
            activityViewModel.musicPlayer.value?.skipNext()
        }
    }
}
