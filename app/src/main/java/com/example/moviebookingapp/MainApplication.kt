package com.example.moviebookingapp

import android.app.Application
import android.content.Context
import com.example.moviebookingapp.api.APIService
import com.example.moviebookingapp.api.RetrofitHelper
import com.example.moviebookingapp.repositry.MovieListRepositry


class MainApplication : Application() {
    lateinit var movieListRepositry: MovieListRepositry

//    lateinit var mContext: Context


    companion object {
        private var instance: MainApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
//        mContext = applicationContext
        initialize()
    }

    private fun initialize() {
        val apiService = RetrofitHelper.getInstance().create(APIService::class.java)
        movieListRepositry = MovieListRepositry(apiService, applicationContext)
    }
}