package com.example.moviebookingapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviebookingapp.MainApplication
import com.example.moviebookingapp.R
import com.example.moviebookingapp.adapter.SeatLayoutAdapter
import com.example.moviebookingapp.data.Constants
import com.example.moviebookingapp.databinding.FragmentSeatSelectionBinding
import com.example.moviebookingapp.utils.NetworkUtils
import com.example.moviebookingapp.viewmodel.MainActivityViewModel
import com.example.moviebookingapp.viewmodel.MainActivityViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class SeatSelectionFragment : Fragment() {

    private var _binding : FragmentSeatSelectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeatSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val repository =
            (MainApplication.applicationContext() as MainApplication).movieListRepositry

        mainActivityViewModel = ViewModelProvider(
            requireActivity(),
            MainActivityViewModelFactory(repository)
        )[MainActivityViewModel::class.java]

        // *************************************************************************************

        binding.movieName.text = Constants.movieName

        val bullet = "&#8226"
        binding.theatreDetails.text =
                Constants.movieDate + " " +
                "${Html.fromHtml(bullet, 0)}" + " " +
                Constants.theatreName + " " + "${Html.fromHtml(bullet, 0)}" +
                " " + Constants.movieTiming

        val seatList : ArrayList<String> = addSeats()
        val seatListSelected : ArrayList<Boolean> = addSeatsSelected()

        val seatLayoutAdapter : SeatLayoutAdapter =
            SeatLayoutAdapter(seatList, seatListSelected, view.context, binding)

        val layoutManager = GridLayoutManager(view.context, 6)

        binding.seatRecyclerView.layoutManager = layoutManager;
        binding.seatRecyclerView.adapter = seatLayoutAdapter;

        // *************************************************************************************

        binding.seatSelectionBackArrow.setOnClickListener {
            view.findNavController().navigate(R.id.action_seatSelectionFragment_to_theatreSelectionFragment)
        }

        // *************************************************************************************

        binding.purchaseTicketsButton.setOnClickListener {
            if(!NetworkUtils.isInternetAvailable(view.context)) {
                findNavController().navigate(R.id.action_seatSelectionFragment_to_noInternetFragment)
            }
            else {
                Toast.makeText(
                    view.context, "Congrats! Your booking is successful.",
                    Toast.LENGTH_SHORT
                ).show()
                Constants.movieSeatsSelected = seatLayoutAdapter.seatSelectedList.joinToString(", ")
                Constants.movieTicketTransactionID =
                    java.util.UUID.randomUUID().toString().substring(0, 12)
                notificationBuilder(view)
                updateDatabase()
                view.findNavController()
                    .navigate(R.id.action_seatSelectionFragment_to_confirmationScreenFragment)
            }
        }
    }

    private fun updateDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            mainActivityViewModel.updateMovieTicketsPurchased()
        }
    }

    private fun notificationBuilder(view: View) {

        val bullet = "&#8226"

        val notificationDetails =
            Constants.movieName + "\n" +
            Constants.movieDate + " " + "${Html.fromHtml(bullet, 0)}" + " " +
            Constants.theatreName + " " + "${Html.fromHtml(bullet, 0)}" + " " +
            Constants.movieTiming + "\nSeats Selected : ${Constants.movieSeatsSelected}"

        CoroutineScope(Dispatchers.IO).launch {
            mainActivityViewModel.createNotification(notificationDetails, view)
        }
    }

    private fun addSeats() : ArrayList<String> {
        val seatList : ArrayList<String> = ArrayList()
        for (i in 0 .. 47) {
            seatList.add(('A' + i / 6).toString() + ((i % 6)+1).toString())
        }
        return seatList
    }

    private fun addSeatsSelected() : ArrayList<Boolean> {
        val seatListSelected : ArrayList<Boolean> = ArrayList()
        for (i in 0 .. 47) {
            if(Random.nextInt(0,3) == 0 )
                seatListSelected.add(true)
            else
                seatListSelected.add(false)
        }
        return seatListSelected
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}