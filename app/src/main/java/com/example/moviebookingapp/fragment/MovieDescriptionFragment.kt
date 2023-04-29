package com.example.moviebookingapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.moviebookingapp.R
import com.example.moviebookingapp.data.Constants
import com.example.moviebookingapp.databinding.FragmentMovieDescriptionBinding
import com.example.moviebookingapp.model.Movies
import com.example.moviebookingapp.utils.LoadandDeletePhotos
import com.google.gson.Gson

class MovieDescriptionFragment : Fragment() {

    private var _binding : FragmentMovieDescriptionBinding? = null
    private val binding get() = _binding!!

    private  var movie : Movies?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setInitialData(view)

        binding.bookTicketsButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("movie", Gson().toJson(movie))
            findNavController().navigate(R.id.action_movieDescriptionFragment_to_theatreSelectionFragment, bundle)
        }

        binding.movieDescriptionBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_movieDescriptionFragment_to_homeScreenFragment)
        }
    }

    private fun setInitialData(view: View) {
        val jsonNote = arguments?.getString("movie")
        if (jsonNote != null) {
            movie = Gson().fromJson<Movies>(jsonNote, Movies::class.java)
            movie?.let {
                Log.d("Tester", movie.toString())
                binding.movieName.text = it.title
                binding.movieDescription.text = it.plot
//                binding.moviePoster.setImageResource(R.drawable.pathaan)

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


                binding.movieRating.text = it.rating
                binding.movieLength.text = it.runtime
                binding.movieFormat.text = it.movieFormat
                binding.movieLanguage.text = it.language
                binding.movieGenre.text = it.genres
            }
        }
        else if(Constants.movies != null) {
            movie = Constants.movies
            movie?.let { Log.d("Tester", movie.toString())
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

                binding.movieRating.text = it.rating
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