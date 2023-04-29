package com.example.moviebookingapp.api

import com.example.moviebookingapp.model.CityListItem
import com.example.moviebookingapp.model.UpcomingMovieListItem
import retrofit2.http.GET
import retrofit2.Response

interface APIService {

    @GET("2171309ef94d8c05ebaf")
    suspend fun getUpcomingMovieList() : Response<List<UpcomingMovieListItem>>

    @GET("a45585d04fc6240374c2")
    suspend fun getCityList() : Response<List<CityListItem>>
}