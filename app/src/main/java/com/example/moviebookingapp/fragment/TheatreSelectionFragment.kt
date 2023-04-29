package com.example.moviebookingapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviebookingapp.R
import com.example.moviebookingapp.adapter.CalendarAdapter
import com.example.moviebookingapp.adapter.TheatreListAdapter
import com.example.moviebookingapp.data.Constants
import com.example.moviebookingapp.data.PushDataIntoCalendarModel
import com.example.moviebookingapp.databinding.FragmentTheatreSelectionBinding
import com.example.moviebookingapp.model.CustomCalendar
import com.example.moviebookingapp.model.Movies
import com.google.gson.Gson

class TheatreSelectionFragment : Fragment() {

    private var _binding : FragmentTheatreSelectionBinding? = null
    private val binding get() = _binding!!

    private  var movie : Movies?= null

    private lateinit var adapter : TheatreListAdapter

    private val pushDataIntoCalendarModel : PushDataIntoCalendarModel = PushDataIntoCalendarModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTheatreSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.theatreSelectionBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_theatreSelectionFragment_to_movieDescriptionFragment)
        }

        val calendarArrayList: ArrayList<CustomCalendar> = pushDataIntoCalendarModel.pushData()

        val calendarAdapter = CalendarAdapter(calendarArrayList, view.context)

        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)

        binding.calendarRecyclerView.layoutManager = layoutManager
        binding.calendarRecyclerView.adapter = calendarAdapter

        setInitialData(view)
    }

    private fun setInitialData(view: View) {
        val jsonNote = arguments?.getString("movie")
        if (jsonNote != null) {
            movie = Gson().fromJson<Movies>(jsonNote, Movies::class.java)
            movie?.let {
                Log.d("Tester", movie.toString())
                binding.movieName.text = movie!!.title

                Constants.movieName = movie!!.title

                adapter = TheatreListAdapter()

                binding.theatreRecyclerView.layoutManager= LinearLayoutManager(view.context)
                binding.theatreRecyclerView.adapter=adapter
                adapter.submitList(movie!!.theatres)
                Log.d("Tester", movie!!.theatres.toString())
            }
        }
        else if(Constants.movies != null) {
            movie = Constants.movies
            movie?.let {
                Log.d("Tester", movie.toString())
                binding.movieName.text = movie!!.title

                Constants.movieName = movie!!.title

                adapter = TheatreListAdapter()

                binding.theatreRecyclerView.layoutManager= LinearLayoutManager(view.context)
                binding.theatreRecyclerView.adapter=adapter
                adapter.submitList(movie!!.theatres)
                Log.d("Tester", movie!!.theatres.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}