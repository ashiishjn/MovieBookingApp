package com.example.moviebookingapp.model

data class Movies(
    val actors: String,
    val director: String,
    val genres: String,
    val id: String,
    val language: String,
    val movieFormat: String,
    val plot: String,
    val posterUrl: String,
    val rating: String,
    val releaseDate: String,
    val runtime: String,
    val theatres: List<Theatre>,
    val title: String
)