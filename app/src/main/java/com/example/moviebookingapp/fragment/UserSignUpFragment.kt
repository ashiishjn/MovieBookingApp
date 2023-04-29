package com.example.moviebookingapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.moviebookingapp.R
import com.example.moviebookingapp.databinding.FragmentUserSignUpBinding
import com.example.moviebookingapp.ui.UserSignUpView
import com.example.moviebookingapp.utils.NetworkUtils

class UserSignUpFragment : Fragment() {

    private val userSignUpView : UserSignUpView = UserSignUpView()

    private  var _binding : FragmentUserSignUpBinding?=null
    private  val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(!NetworkUtils.isInternetAvailable(view.context)) {
            findNavController().navigate(R.id.action_userSignUpFragment_to_noInternetFragment)
        }
        userSignUpView.workWithListener(view, binding)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}