package com.example.moviebookingapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebookingapp.databinding.CustomMovieCardBinding
import com.example.moviebookingapp.model.Movies
import com.example.moviebookingapp.utils.LoadandDeletePhotos

class MovieListAdapter(private val onNoteClicked: (Movies) -> Unit, context: Context) :
    ListAdapter<Movies, MovieListAdapter.MovieViewHolder>(ComparatorDiffUtil()) {

    val context : Context

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        Log.d("Application","Inside List Adapter")
        val binding = CustomMovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(it)
        }
    }

    inner class MovieViewHolder(private val binding: CustomMovieCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movies) {
            binding.homeScreenMovieRating.text = movie.rating
            binding.movieName.text = movie.title
            val photo = LoadandDeletePhotos.loadPhotosFromInternalStorage(context, movie.title)
            if (photo.isNotEmpty()) {
                binding.homeScreenMoviePosterImage.setImageBitmap(photo[0].bitmap)
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
                    binding.homeScreenMoviePosterImage
                )
            }
            binding.root.setOnClickListener {
                onNoteClicked(movie)
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Movies>() {
        override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem == newItem
        }
    }
}