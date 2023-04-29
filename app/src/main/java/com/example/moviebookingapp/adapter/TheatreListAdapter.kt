package com.example.moviebookingapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebookingapp.databinding.CustomTheatreLayoutBinding
import com.example.moviebookingapp.model.Theatre
import androidx.recyclerview.widget.ListAdapter as ListAdapter

class TheatreListAdapter():
    ListAdapter<Theatre, TheatreListAdapter.TheatreViewHolder>(ComparatorDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheatreViewHolder {
        val binding = CustomTheatreLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TheatreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TheatreViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(it)
        }
    }

    inner class TheatreViewHolder(private val binding: CustomTheatreLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(theatre: Theatre) {
            binding.theatreName.text=theatre.name



            val theatreTimingAdapter : TimingsListAdapter = TimingsListAdapter(theatre.timings,
                theatre.name, binding.root.context)
//
            val theatreTimingLayoutManager = GridLayoutManager(binding.root.context, 3)
            binding.theatreTimingsRecyclerView.layoutManager = theatreTimingLayoutManager
            binding.theatreTimingsRecyclerView.adapter = theatreTimingAdapter
        }

    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Theatre>() {
        override fun areItemsTheSame(oldItem: Theatre, newItem: Theatre): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Theatre, newItem: Theatre): Boolean {
            return oldItem == newItem
        }
    }
}