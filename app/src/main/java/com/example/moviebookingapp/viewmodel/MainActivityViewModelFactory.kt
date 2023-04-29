package com.example.moviebookingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviebookingapp.repositry.MovieListRepositry

class MainActivityViewModelFactory(private val movieListRepositry : MovieListRepositry) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(movieListRepositry) as T
    }
}