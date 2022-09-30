package com.example.evagnelyrics.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.evagnelyrics.R
import com.example.evagnelyrics.databinding.ImageRecyclerLayoutBinding

class PictureAdapter : RecyclerView.Adapter<PictureAdapter.ViewHolder>() {
    private val items = listOf(
        R.drawable.img1_7185,
        R.drawable.img2_7190,
        R.drawable.img3_7228,
        R.drawable.img4_7236,
        R.drawable.img5_7281,
    )

    inner class ViewHolder(private val binding: ImageRecyclerLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.run {
                image.setImageResource(items[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ImageRecyclerLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int = items.count()
}