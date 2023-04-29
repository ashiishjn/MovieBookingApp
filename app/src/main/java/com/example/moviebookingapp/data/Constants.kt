package com.example.moviebookingapp.data

import com.example.moviebookingapp.model.Event
import com.example.moviebookingapp.model.Movies
import com.example.moviebookingapp.model.UpcomingMovieListItem

class Constants {
    companion object {
        var movieName : String = ""
        var theatreName : String = ""
        var movieTiming : String = ""
        var movieDate : String = ""
        var movieSeatsSelected : String = ""
        var movieTicketTransactionID : String = ""

        const val CHANNEL_ID : String = "CHANNEL_ID_01"
        const val NOTIFICATION_ID : Int = 1

        var eventName : String = ""
        var eventDate : String = ""
        var eventLocation : String = ""
        var eventSeats : String = ""
        var eventTransactionId : String = ""

        var movies : Movies? = null
        var event : Event? = null
        var upcomingMovie : UpcomingMovieListItem? = null
    }
}