package com.android.example.music.ui.settingsfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.android.example.music.R
import com.android.example.music.databinding.SongRowBinding
import com.android.example.music.models.Song

class SettingsListAdapter(
    private val data: List<Song>,
    private val context: Context,
    private val activity: FragmentActivity,
    private val callBack: (Int) -> Unit
) : RecyclerView.Adapter<SettingsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = SongRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SettingsListAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(private val binding: SongRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val songRow = binding.rowSong
        val isInPlaylistObserver = Observer<Boolean> { isInPlaylist ->
            if (isInPlaylist) {
                songRow.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.pink)
            } else {
                songRow.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.light_dark)
            }
        }

        fun bind(position: Int) {
            val item = data[position]
            item.isInPlaylist.observe(activity, isInPlaylistObserver)
            binding.rowNamesong.text = item.name
            songRow.setOnClickListener {
                callBack(item.index)
            }
        }
    }
}
