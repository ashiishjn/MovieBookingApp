package com.example.moviebookingapp.model

data class CityListItem(
    val city: String,
    val id: Int,
    val movies: List<Movies>,
    val events: List<Event>
)