package com.example.moviebookingapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.moviebookingapp.R
import com.example.moviebookingapp.databinding.FragmentNoInternetBinding
import com.example.moviebookingapp.databinding.FragmentProfileBinding
import com.example.moviebookingapp.utils.NetworkUtils

class NoInternetFragment : Fragment() {

    private var _binding : FragmentNoInternetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoInternetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.retry.setOnClickListener {
            binding.progressBar.visibility =View.VISIBLE
            if(NetworkUtils.isInternetAvailable(view.context)) {
                binding.progressBar.visibility =View.INVISIBLE
                findNavController().navigate(R.id.action_noInternetFragment_to_homeScreenFragment)
            } else {
                binding.progressBar.visibility =View.INVISIBLE
                Toast.makeText(view.context, "No network connection found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}