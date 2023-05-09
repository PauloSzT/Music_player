package com.android.example.music.ui.homefragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.example.music.databinding.SongRowBinding
import com.android.example.music.models.Song

class SongListAdapter(
    private val data: List<Song>,
    private val context: Context,
    private val callBack: (Int) -> Unit
) : RecyclerView.Adapter<SongListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = SongRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(private val binding: SongRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val songRow = binding.rowSong

        fun bind(position: Int) {
            val item = data[position]
            binding.rowNamesong.text = item.name
            songRow.setOnClickListener {
                callBack(item.index)
            }
        }
    }
}
