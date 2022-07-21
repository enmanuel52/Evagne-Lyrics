package com.example.evagnelyrics.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.evagnelyrics.R
import com.example.evagnelyrics.data.database.entities.LyricsEntity
import com.example.evagnelyrics.databinding.ListRecyclerLayoutBinding

class ListAdapter(val listener: (Action) -> Unit) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    var items: List<LyricsEntity> = emptyList()

    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ListRecyclerLayoutBinding.bind(view)
        fun onBind() {
            binding.root.setOnClickListener { listener(
                Action.Click(title = items[adapterPosition].title)
            ) }

            binding.favorite.setOnClickListener {
                listener(Action.Favorite(items[adapterPosition].title))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_recycler_layout, parent, false)
        return ListViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onBind()
        val current = items[position]
        holder.binding.songTitle.text = current.title
        holder.binding.favorite.apply {
            if(current.favorite)
                setImageResource(R.drawable.ic_round_favorite_24)
            else
                setImageResource(R.drawable.ic_round_favorite_white_24)
        }
    }

    override fun getItemCount(): Int = items.count()
}

sealed class Action {
    data class Click(val title: String) : Action()
    data class Favorite(val title: String) : Action()
}