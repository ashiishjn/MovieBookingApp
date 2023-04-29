package com.example.moviebookingapp.model

data class TicketsPurchased(
    val type : String,
    val date : String,
    val name : String,
    val seatSelected : String,
    val location : String,
    val transactionID : String
)
