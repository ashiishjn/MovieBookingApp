package com.example.moviebookingapp.repositry

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.moviebookingapp.api.APIService
import com.example.moviebookingapp.data.Constants
import com.example.moviebookingapp.model.*
import com.example.moviebookingapp.utils.NetworkResult
import com.example.moviebookingapp.utils.NetworkUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MovieListRepositry(
    private val apiService: APIService,
    private val applicationContext: Context) {

    private lateinit var reference : DatabaseReference
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _upcomingMovieListLiveData = MutableLiveData<NetworkResult<List<UpcomingMovieListItem>>>()
    val upcomingMovieListLiveData get() = _upcomingMovieListLiveData

    private val _cityListLiveData = MutableLiveData<NetworkResult<List<CityListItem>>>()
    val cityListLiveData get() = _cityListLiveData

    private val _userDetailsLiveData = MutableLiveData<UserDetails>()
    val userDetailsLiveData get() = _userDetailsLiveData

    suspend fun getUpcomingMovieList() {
        if(NetworkUtils.isInternetAvailable(applicationContext)) {
            try {
                val result = apiService.getUpcomingMovieList()
                if(result?.body() != null) {
                    upcomingMovieListLiveData.postValue(NetworkResult.Success(result.body()!!))
                }
            }
            catch (e : java.lang.Exception){
                upcomingMovieListLiveData.postValue(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    suspend fun getCityList() {
        if(NetworkUtils.isInternetAvailable(applicationContext)) {
            try {
                val result = apiService.getCityList()
                if(result?.body() != null) {
                    cityListLiveData.postValue(NetworkResult.Success(result.body()!!))
                }
            }
            catch (e : java.lang.Exception){
                cityListLiveData.postValue(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    suspend fun updateMovieTicketsPurchased() {

        reference = FirebaseDatabase.getInstance().getReference("Users")
            .child(auth.uid.toString()).child("Tickets Purchased")

        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val current = LocalDateTime.now().format(formatter)
        reference.child(current).child("Name").setValue(Constants.movieName)
        reference.child(current).child("Location").setValue(Constants.theatreName)
        reference.child(current).child("Date").setValue(Constants.movieDate + ", " + Constants.movieTiming)
        reference.child(current).child("Seats").setValue(Constants.movieSeatsSelected)
        reference.child(current).child("Transaction ID").setValue(Constants.movieTicketTransactionID)
        reference.child(current).child("Type").setValue("movie")

    }

    suspend fun updateEventTicketsPurchased() {

        reference = FirebaseDatabase.getInstance().getReference("Users")
            .child(auth.uid.toString()).child("Tickets Purchased")

        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val current = LocalDateTime.now().format(formatter)
        reference.child(current).child("Name").setValue(Constants.eventName)
        reference.child(current).child("Location").setValue(Constants.eventLocation)
        reference.child(current).child("Date").setValue(Constants.eventDate)
        reference.child(current).child("Seats").setValue(Constants.eventSeats)
        reference.child(current).child("Transaction ID").setValue(Constants.eventTransactionId)
        reference.child(current).child("Type").setValue("event")

    }

    suspend fun getDataFromFirebase() {

        val listTicketsPurchased : MutableList<TicketsPurchased> = ArrayList()

        reference = FirebaseDatabase.getInstance().getReference("Users")
            .child(auth.uid.toString())

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                    val userDetails = UserDetails(
                        snapshot.child("Email Id").getValue(String::class.java)!!,
                        snapshot.child("Mobile Number").getValue(String::class.java)!!,
                        snapshot.child("Name").getValue(String::class.java)!!,
                        listTicketsPurchased
                    )
                    userDetailsLiveData.postValue(userDetails)
                    Log.d("Tester", userDetails.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        // *****************************************************************************

        reference = FirebaseDatabase.getInstance().getReference("Users")
            .child(auth.uid.toString()).child("Tickets Purchased")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children) {

                    val ticketsPurchased = TicketsPurchased(
                        data.child("Type").getValue(String::class.java)!!,
                        data.child("Date").getValue(String::class.java)!!,
                        data.child("Name").getValue(String::class.java)!!,
                        data.child("Seats").getValue(String::class.java)!!,
                        data.child("Location").getValue(String::class.java)!!,
                        data.child("Transaction ID").getValue(String::class.java)!!
                    )
                    listTicketsPurchased.add(ticketsPurchased)
                }
                    Log.d("Tester", listTicketsPurchased.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        // *****************************************************************************

//        reference = FirebaseDatabase.getInstance().getReference("Users")
//            .child(auth.uid.toString()).child("Event Tickets Purchased")
//
//        reference.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                for (data in snapshot.children) {
//
//                    val eventTickets = EventTicketsPurchased(
//                        data.child("Date").getValue(String::class.java)!!,
//                        data.child("Event Name").getValue(String::class.java)!!,
//                        data.child("Location").getValue(String::class.java)!!,
//                        data.child("Seats").getValue(String::class.java)!!,
//                        data.child("Transaction ID").getValue(String::class.java)!!
//                    )
//                    listEventTicketsPurchased.add(eventTickets)
//                }
//                    Log.d("Tester", listEventTicketsPurchased.toString())
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//        })

    }

}