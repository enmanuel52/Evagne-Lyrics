package com.example.evagnelyrics.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.evagnelyrics.R
import com.example.evagnelyrics.databinding.GalleryRecyclerLayoutBinding

class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    private val items = listOf(
        R.drawable.img1_7185,
        R.drawable.img2_7190,
        R.drawable.img3_7228,
        R.drawable.img4_7236,
        R.drawable.img5_7281,
    )

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = GalleryRecyclerLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.gallery_recycler_layout, parent, false)
        return ViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.photo.setImageResource(items[position])
    }

    override fun getItemCount(): Int = items.count()
}