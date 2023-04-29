package com.example.moviebookingapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moviebookingapp.MainApplication
import com.example.moviebookingapp.R
import com.example.moviebookingapp.data.Constants
import com.example.moviebookingapp.databinding.FragmentEventBookingBinding
import com.example.moviebookingapp.model.Event
import com.example.moviebookingapp.utils.LoadandDeletePhotos
import com.example.moviebookingapp.utils.NetworkUtils
import com.example.moviebookingapp.viewmodel.MainActivityViewModel
import com.example.moviebookingapp.viewmodel.MainActivityViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventBookingFragment : Fragment() {

    private lateinit var reference: DatabaseReference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var _binding: FragmentEventBookingBinding? = null
    private val binding get() = _binding!!

    private var event: Event? = null

    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.tncArrow.setOnClickListener {
            if(binding.eventTnc.visibility == View.GONE) {
                binding.eventTnc.visibility = View.VISIBLE
                binding.tncArrow.setImageResource(R.drawable.va_baseline_keyboard_arrow_dark_up_24)
            }
            else {
                binding.eventTnc.visibility = View.GONE
                binding.tncArrow.setImageResource(R.drawable.va_baseline_keyboard_arrow_dark_down_24)
            }
        }

        val repository =
            (MainApplication.applicationContext() as MainApplication).movieListRepositry

        mainActivityViewModel = ViewModelProvider(
            requireActivity(),
            MainActivityViewModelFactory(repository)
        )[MainActivityViewModel::class.java]

        // *************************************************************************************

        setInitialData(view)
        bookTickets()

        reference = FirebaseDatabase.getInstance().getReference("Users")
            .child(auth.uid.toString()).child("Event Tickets Purchased")

        binding.eventDescriptionBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_eventBookingFragment_to_homeScreenFragment)
        }

        binding.eventTicketsButton.setOnClickListener {
            if (!NetworkUtils.isInternetAvailable(view.context)) {
                findNavController().navigate(R.id.action_eventBookingFragment_to_noInternetFragment)
            } else {
                Toast.makeText(
                    view.context,
                    "Thank you! \nYour purchase was successful.",
                    Toast.LENGTH_SHORT
                ).show()
                onNoteClicked()
                notificationBuilder(view)
                updateDatabase()
            }
        }
    }

    private fun setInitialData(view : View) {
        val jsonNote = arguments?.getString("event")
        if (jsonNote != null) {
            event = Gson().fromJson<Event>(jsonNote, Event::class.java)
            event?.let {
                binding.eventName.text = it.name
                binding.eventType.text = it.type

                val photo = LoadandDeletePhotos.loadPhotosFromInternalStorage(view.context, it.name)
                if (photo.isNotEmpty()) {
                    binding.eventPoster.setImageBitmap(photo[0].bitmap)
                    Log.d("Tester2", photo.toString())
                }
                else {
                    LoadandDeletePhotos.loadImageIntoInternalStorage(
                        view.context,
                        it.posterUrl,
                        it.name
                    )

                    LoadandDeletePhotos.loadImage(
                        it.posterUrl,
                        binding.eventPoster
                    )
                }

                binding.eventHost.text = it.host
                binding.eventTime.text = it.time
                binding.eventLocation.text = it.location
                binding.eventDuration.text = it.duration
                binding.eventLanguage.text = it.language
                binding.eventDetails.text = it.description
                binding.eventTnc.text = it.tnc
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bookTickets() {
        binding.ticketAdd.setOnClickListener {
            if (binding.ticketCount.text.toString().toInt() == 0) {
                binding.ticketCount.text = "1"
                binding.eventTicketsButton.visibility = View.VISIBLE
                binding.eventTicketsButton.text = "Pay ₹150"
                binding.ticketSubtract.setImageResource(R.drawable.va_baseline_remove_circle_dark_outline_24)
            } else if (binding.ticketCount.text.toString().toInt() < 9) {
                binding.ticketCount.text =
                    (binding.ticketCount.text.toString().toInt() + 1).toString()
                val amt = binding.ticketCount.text.toString().toInt() * 150
                binding.eventTicketsButton.text = "Pay ₹$amt"
            } else if (binding.ticketCount.text.toString().toInt() == 9) {
                binding.ticketCount.text = "10"
                binding.eventTicketsButton.text = "Pay ₹1500"
                binding.ticketAdd.setImageResource(R.drawable.va_baseline_add_circle_outline_24)
            }
        }

        binding.ticketSubtract.setOnClickListener {
            if (binding.ticketCount.text.toString().toInt() == 10) {
                binding.ticketCount.text = "9"
                binding.eventTicketsButton.text = "Pay ₹1350"
                binding.ticketAdd.setImageResource(R.drawable.va_baseline_add_circle_dark_outline_24)
            } else if (binding.ticketCount.text.toString().toInt() > 1) {
                binding.ticketCount.text =
                    (binding.ticketCount.text.toString().toInt() - 1).toString()
                val amt = binding.ticketCount.text.toString().toInt() * 150
                binding.eventTicketsButton.text = "Pay ₹$amt"
            } else if (binding.ticketCount.text.toString().toInt() == 1) {
                binding.ticketCount.text = "0"
                binding.eventTicketsButton.visibility = View.INVISIBLE
                binding.ticketSubtract.setImageResource(R.drawable.va_baseline_remove_circle_outline_24)
            }
        }
    }

    private fun onNoteClicked() {
        Constants.eventName = binding.eventName.text.toString()
        Constants.eventDate = binding.eventTime.text.toString()
        Constants.eventLocation = binding.eventLocation.text.toString()
        Constants.eventSeats = binding.ticketCount.text.toString()
        Constants.eventTransactionId = java.util.UUID.randomUUID().toString().substring(0, 12)
        val bundle = Bundle()
        bundle.putString("id", Constants.eventTransactionId)
        bundle.putString("name", Constants.eventName)
        bundle.putString("date", Constants.eventDate)
        bundle.putString("location", Constants.eventLocation)
        bundle.putString("seats", Constants.eventSeats)
        findNavController().navigate(
            R.id.action_eventBookingFragment_to_eventConfirmationScreenFragment,
            bundle
        )
    }

    private fun updateDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            mainActivityViewModel.updateEventTicketsPurchased()
        }
    }

    private fun notificationBuilder(view: View) {
        val notificationDetails =
            Constants.eventName + "\n" +
                    Constants.eventDate + "\n" +
                    Constants.eventLocation + "\n" +
                    "No. of seats selected : ${Constants.eventSeats}"

        CoroutineScope(Dispatchers.IO).launch {
            mainActivityViewModel.createNotification(notificationDetails, view)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}