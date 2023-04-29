package com.example.moviebookingapp.viewmodel;

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebookingapp.model.CityListItem
import com.example.moviebookingapp.model.UpcomingMovieListItem
import com.example.moviebookingapp.model.UserDetails
import com.example.moviebookingapp.repositry.MovieListRepositry;
import com.example.moviebookingapp.utils.NetworkResult
import com.example.moviebookingapp.utils.NotificationBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(private val movieListRepositry :MovieListRepositry) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO){
            movieListRepositry.getCityList()
            movieListRepositry.getUpcomingMovieList()
            movieListRepositry.getDataFromFirebase()
        }
    }

    suspend fun updateMovieTicketsPurchased() {
        movieListRepositry.updateMovieTicketsPurchased()
    }

    suspend fun updateEventTicketsPurchased() {
        movieListRepositry.updateEventTicketsPurchased()
    }

    private val notificationBuilder : NotificationBuilder = NotificationBuilder()
    suspend fun createNotification(notificationDetails : String, view: View) {
        notificationBuilder.notificationBuilder(notificationDetails, view)
    }

    suspend fun updateFromDatabase() {
        movieListRepositry.getDataFromFirebase()
    }

    val upcomingMovieList : LiveData<NetworkResult<List<UpcomingMovieListItem>>>
        get() = movieListRepositry.upcomingMovieListLiveData

    val cityList : LiveData<NetworkResult<List<CityListItem>>>
        get() = movieListRepositry.cityListLiveData

    val userDetails : LiveData<UserDetails>
        get() = movieListRepositry.userDetailsLiveData

}
