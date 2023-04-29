package com.example.moviebookingapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviebookingapp.MainApplication
import com.example.moviebookingapp.R
import com.example.moviebookingapp.adapter.EventListAdapter
import com.example.moviebookingapp.adapter.MovieListAdapter
import com.example.moviebookingapp.adapter.UpcomingMovieListAdapter
import com.example.moviebookingapp.data.Constants
import com.example.moviebookingapp.databinding.FragmentHomeScreenBinding
import com.example.moviebookingapp.model.*
import com.example.moviebookingapp.utils.NetworkResult
import com.example.moviebookingapp.utils.NetworkUtils
import com.example.moviebookingapp.viewmodel.MainActivityViewModel
import com.example.moviebookingapp.viewmodel.MainActivityViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

class HomeScreenFragment : Fragment() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private var _binding : FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var movieListAdapter: MovieListAdapter

    private lateinit var upcomingMovieListAdapter: UpcomingMovieListAdapter

    private lateinit var eventListAdapter: EventListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if(!NetworkUtils.isInternetAvailable(view.context)) {
            findNavController().navigate(R.id.action_homeScreenFragment_to_noInternetFragment)
        }

        // *************************************************************************************

        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.history -> {
                    findNavController().navigate(R.id.action_homeScreenFragment_to_purchaseHistoryFragment)
                }
                R.id.profile -> {
                    findNavController().navigate(R.id.action_homeScreenFragment_to_profileFragment)
                }
                R.id.signout -> {
                    auth.signOut()
                    view.findNavController().navigate(R.id.action_homeScreenFragment_to_userSignInFragment)
                }
            }
            true
        }

        // *************************************************************************************

        val repository =
            (MainApplication.applicationContext() as MainApplication).movieListRepositry

        mainActivityViewModel = ViewModelProvider(
            requireActivity(),
            MainActivityViewModelFactory(repository)
        )[MainActivityViewModel::class.java]

        // *************************************************************************************

        upcomingMovieListAdapter = UpcomingMovieListAdapter(::onNoteClicked, view.context)

        binding.upcomingMovieListRecyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        binding.upcomingMovieListRecyclerView.adapter = upcomingMovieListAdapter

        // *************************************************************************************

        mainActivityViewModel.upcomingMovieList.observe(viewLifecycleOwner) { it ->
            when (it) {
                is NetworkResult.Loading -> {
                    Log.d("Tester", "Loading")
                }
                is NetworkResult.Success -> {
                    it.data?.let {
                        upcomingMovieListAdapter.submitList(it)
                        Log.d("Tester", it.toString())
                        binding.upcomingMovies.visibility = View.VISIBLE
                    }
                }
                is NetworkResult.Error -> {
                    it.message?.let {
                        Log.d("Tester", it)
                    }
                }
            }
        }

        // *************************************************************************************

        mainActivityViewModel.cityList.observe(viewLifecycleOwner) { it ->
            when (it) {
                is NetworkResult.Loading -> {
                    Log.d("Tester", "Loading")
                }
                is NetworkResult.Success -> {
                    it.data?.let {
                        showSpinner(view, it)
                        Log.d("Tester", it.toString())
                    }
                }
                is NetworkResult.Error -> {
                    it.message?.let {
                        Log.d("Tester", it)
                    }
                }
            }
        }

        // *************************************************************************************

        mainActivityViewModel.userDetails.observe(viewLifecycleOwner) {
            val tView = binding.navigationView.getHeaderView(0)
            if(tView != null)
                tView.findViewById<TextView>(R.id.user_name).text = it.Name
        }

        // *************************************************************************************

        binding.hamburgerIcon.setOnClickListener {
                binding.homeScreenDrawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navigationView.getHeaderView(0).findViewById<ImageView>(R.id.back_arrow).setOnClickListener {
            binding.homeScreenDrawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun showSpinner(vieww : View, cityList : List<CityListItem>) {
        val cityNames : ArrayList<String> = ArrayList()
        for(city in cityList)
            cityNames.add(city.city)
        val spinnerAdapter = ArrayAdapter(vieww.context, R.layout.custom_spinner_layout,cityNames)
        binding.spinnerCitySelect.adapter = spinnerAdapter

        binding.spinnerCitySelect.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                showRecyclerView(vieww, cityList[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun showRecyclerView(view: View, cityListItem: CityListItem) {
        binding.recommendedMovies.visibility = View.VISIBLE
        movieListAdapter = MovieListAdapter(::onNoteClicked, view.context)

        binding.movieListRecyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        binding.movieListRecyclerView.adapter = movieListAdapter
        movieListAdapter.submitList(cityListItem.movies)

        // *************************************************************************************

        binding.recommendedShows.visibility = View.VISIBLE
        eventListAdapter = EventListAdapter(::onNoteClicked, view.context)

        binding.eventListRecyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        binding.eventListRecyclerView.adapter = eventListAdapter
        eventListAdapter.submitList(cityListItem.events)
    }

    private fun onNoteClicked(movie: Movies){
        val bundle = Bundle()
        bundle.putString("movie", Gson().toJson(movie))
        Constants.movies = movie
        findNavController().navigate(R.id.action_homeScreenFragment_to_movieDescriptionFragment, bundle)
    }

    private fun onNoteClicked(event: Event){
        val bundle = Bundle()
        bundle.putString("event", Gson().toJson(event))
        Constants.event = event
        findNavController().navigate(R.id.action_homeScreenFragment_to_eventBookingFragment, bundle)
    }

    private fun onNoteClicked(movie: UpcomingMovieListItem){
        val bundle = Bundle()
        bundle.putString("movie", Gson().toJson(movie))
        Constants.upcomingMovie = movie
        findNavController().navigate(R.id.action_homeScreenFragment_to_upcomingMovieDescriptionFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}