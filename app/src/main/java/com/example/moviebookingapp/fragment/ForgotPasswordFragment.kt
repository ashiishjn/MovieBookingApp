package com.example.moviebookingapp.fragment

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.moviebookingapp.R
import com.example.moviebookingapp.databinding.FragmentConfirmationScreenBinding
import com.example.moviebookingapp.databinding.FragmentForgotPasswordBinding
import com.example.moviebookingapp.utils.NetworkUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordFragment : Fragment() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private var _binding : FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if(!NetworkUtils.isInternetAvailable(view.context)) {
            findNavController().navigate(R.id.action_forgotPasswordFragment_to_noInternetFragment)
        }

        binding.resetPassword.setOnClickListener {
            if(!NetworkUtils.isInternetAvailable(view.context)) {
                findNavController().navigate(R.id.action_forgotPasswordFragment_to_noInternetFragment)
            }
            else if (Patterns.EMAIL_ADDRESS.matcher(binding.emailId.text.toString()).matches()) {
                auth.sendPasswordResetEmail(
                    binding.emailId.text.toString()
                )
                    .addOnSuccessListener {
                        Toast.makeText(
                            view.context,
                            "Password reset link sent to email",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_forgotPasswordFragment_to_userSignInFragment)
                    }
                    .addOnFailureListener {
                        Toast.makeText(view.context, "Some Error has occurred", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
            else
                Toast.makeText(view.context, "Enter valid email", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}