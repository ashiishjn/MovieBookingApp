package com.example.moviebookingapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebookingapp.R
import com.example.moviebookingapp.databinding.CustomUpcomingMovieCardBinding
import com.example.moviebookingapp.model.UpcomingMovieListItem
import com.example.moviebookingapp.utils.LoadandDeletePhotos

class UpcomingMovieListAdapter(private val onNoteClicked: (UpcomingMovieListItem) -> Unit, context : Context) :
    ListAdapter<UpcomingMovieListItem, UpcomingMovieListAdapter.MovieViewHolder>(ComparatorDiffUtil()) {

    val context : Context

    init {
        this.context = context
    }

//    val moviePoster : MoviePoster = MoviePoster()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        Log.d("Application","Inside List Adapter")
        val binding = CustomUpcomingMovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(it)
        }
    }

    inner class MovieViewHolder(private val binding: CustomUpcomingMovieCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: UpcomingMovieListItem) {

            binding.movieName.text = movie.title
            val photo = LoadandDeletePhotos.loadPhotosFromInternalStorage(context, movie.title)
            if (photo.isNotEmpty()) {
                binding.homeScreenUpcomingMoviePosterImage.setImageBitmap(photo[0].bitmap)
                Log.d("Tester2", photo.toString())
            }
            else {
                LoadandDeletePhotos.loadImageIntoInternalStorage(
                    context,
                    movie.posterUrl,
                    movie.title
                )

                LoadandDeletePhotos.loadImage(
                    movie.posterUrl,
                    binding.homeScreenUpcomingMoviePosterImage
                )
            }
            binding.root.setOnClickListener {
                onNoteClicked(movie)
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<UpcomingMovieListItem>() {
        override fun areItemsTheSame(oldItem: UpcomingMovieListItem, newItem: UpcomingMovieListItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: UpcomingMovieListItem, newItem: UpcomingMovieListItem): Boolean {
            return oldItem == newItem
        }
    }
}