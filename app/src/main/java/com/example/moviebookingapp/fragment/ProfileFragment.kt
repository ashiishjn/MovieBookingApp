package com.example.moviebookingapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moviebookingapp.MainApplication
import com.example.moviebookingapp.R
import com.example.moviebookingapp.databinding.FragmentProfileBinding
import com.example.moviebookingapp.model.UserDetails
import com.example.moviebookingapp.viewmodel.MainActivityViewModel
import com.example.moviebookingapp.viewmodel.MainActivityViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val repository =
            (MainApplication.applicationContext() as MainApplication).movieListRepositry

        mainActivityViewModel = ViewModelProvider(
            requireActivity(),
            MainActivityViewModelFactory(repository)
        )[MainActivityViewModel::class.java]

        mainActivityViewModel.userDetails.observe(viewLifecycleOwner) {
            binding.userName.text = it.Name
            binding.userMailid.text = it.email_Id
            binding.userNumber.text = it.mobile_Number
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homeScreenFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}