package com.example.moviebookingapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.moviebookingapp.R
import com.example.moviebookingapp.databinding.FragmentUserSignInBinding
import com.example.moviebookingapp.ui.UserSignInView
import com.example.moviebookingapp.utils.NetworkUtils

class UserSignInFragment : Fragment() {

    private  var _binding : FragmentUserSignInBinding?=null
    private  val binding get() = _binding!!

    private val userSignInView = UserSignInView()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(!NetworkUtils.isInternetAvailable(view.context)) {
            findNavController().navigate(R.id.action_userSignInFragment_to_noInternetFragment)
        }
        userSignInView.workWithListener(view, binding)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}