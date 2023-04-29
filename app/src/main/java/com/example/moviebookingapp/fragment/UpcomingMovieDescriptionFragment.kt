package com.example.moviebookingapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.moviebookingapp.R
import com.example.moviebookingapp.databinding.FragmentUpcomingMovieDescriptionBinding
import com.example.moviebookingapp.model.UpcomingMovieListItem
import com.example.moviebookingapp.utils.LoadandDeletePhotos
import com.google.gson.Gson

class UpcomingMovieDescriptionFragment : Fragment() {

    private var _binding : FragmentUpcomingMovieDescriptionBinding? = null
    private val binding get() = _binding!!

    private  var movie : UpcomingMovieListItem?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpcomingMovieDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setInitialData(view)

        binding.movieDescriptionBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_upcomingMovieDescriptionFragment_to_homeScreenFragment)
        }
    }

    private fun setInitialData(view: View) {
        val jsonNote = arguments?.getString("movie")
        if (jsonNote != null) {
            movie = Gson().fromJson<UpcomingMovieListItem>(jsonNote, UpcomingMovieListItem::class.java)
            movie?.let {
                binding.movieName.text = it.title
                binding.movieDescription.text = it.plot

                val photo = LoadandDeletePhotos.loadPhotosFromInternalStorage(view.context, it.title)
                if (photo.isNotEmpty()) {
                    binding.moviePoster.setImageBitmap(photo[0].bitmap)
                    Log.d("Tester2", photo.toString())
                }
                else {
                    LoadandDeletePhotos.loadImageIntoInternalStorage(
                        view.context,
                        it.posterUrl,
                        it.title
                    )

                    LoadandDeletePhotos.loadImage(
                        it.posterUrl,
                        binding.moviePoster
                    )
                }

                binding.movieLength.text = it.runtime
                binding.movieFormat.text = it.movieFormat
                binding.movieLanguage.text = it.language
                binding.movieGenre.text = it.genres
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}