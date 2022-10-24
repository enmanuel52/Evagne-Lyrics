package com.example.evagnelyrics.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.evagnelyrics.data.model.MainItem
import javax.inject.Inject

class MainAdapter @Inject constructor(
    _items: List<MainItem>
): RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    private val items: List<MainItem>

    init {
        items = _items
    }

    inner class MainViewHolder(view: View): RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = items.count()
}