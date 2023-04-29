package com.example.moviebookingapp.model

data class UserDetails(
    var email_Id: String,
    val mobile_Number: String,
    val Name: String,
    val tickets_Purchased: List<TicketsPurchased>,
)