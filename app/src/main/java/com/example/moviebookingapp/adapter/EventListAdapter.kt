package com.example.moviebookingapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebookingapp.R
import com.example.moviebookingapp.data.Constants
import com.example.moviebookingapp.databinding.CustomEventCardBinding
import com.example.moviebookingapp.model.Event
import com.example.moviebookingapp.utils.LoadandDeletePhotos

class EventListAdapter(private val onNoteClicked: (Event) -> Unit, context: Context) :
    ListAdapter<Event, EventListAdapter.EventViewHolder>(ComparatorDiffUtil()) {

    val context : Context

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        Log.d("Application","Inside List Adapter")
        val binding = CustomEventCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(it)
        }
    }

    inner class EventViewHolder(private val binding: CustomEventCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            binding.eventDate.text = event.time.substring(0, 10)
            binding.eventName.text = event.name

            val photo = LoadandDeletePhotos.loadPhotosFromInternalStorage(context, event.name)
            if (photo.isNotEmpty()) {
                binding.homeScreenEventPosterImage.setImageBitmap(photo[0].bitmap)
                Log.d("Tester2", photo.toString())
            }
            else {
                LoadandDeletePhotos.loadImageIntoInternalStorage(
                    context,
                    event.posterUrl,
                    event.name
                )

                LoadandDeletePhotos.loadImage(
                    event.posterUrl,
                    binding.homeScreenEventPosterImage
                )
            }

            binding.root.setOnClickListener {
                Constants.eventName = event.name
                onNoteClicked(event)
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}