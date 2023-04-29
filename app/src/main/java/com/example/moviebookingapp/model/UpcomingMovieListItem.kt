package com.example.moviebookingapp.model

data class UpcomingMovieListItem(
    val id: Int,
    val actors: String,
    val director: String,
    val genres: String,
    val language: String,
    val movieFormat: String,
    val plot: String,
    val posterUrl: String,
    val releaseDate: String,
    val runtime: String,
    val title: String
)