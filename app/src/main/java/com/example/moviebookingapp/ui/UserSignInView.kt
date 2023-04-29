package com.example.moviebookingapp.ui

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.moviebookingapp.R
import com.example.moviebookingapp.databinding.FragmentUserSignInBinding
import com.example.moviebookingapp.utils.NetworkUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class UserSignInView {


    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    fun workWithListener(view : View, binding : FragmentUserSignInBinding) {

        if(checkIfUserSignedIn())
            view.findNavController().navigate(R.id.action_userSignInFragment_to_homeScreenFragment)

        var flag = true

        binding.loginShowpassword.setImageResource(R.drawable.va_baseline_visibility_off_24)

        binding.loginShowpassword.setOnClickListener {
            if (flag) {
                binding.loginShowpassword.setImageResource(R.drawable.va_baseline_visibility_24)
                binding.loginPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                flag = false
            } else {
                binding.loginShowpassword.setImageResource(R.drawable.va_baseline_visibility_off_24)
                binding.loginPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                flag = true
            }
        }

        binding.loginButton.setOnClickListener {

            if(!NetworkUtils.isInternetAvailable(view.context)) {
                view.findNavController().navigate(R.id.action_userSignInFragment_to_noInternetFragment)
            }
            else if(binding.loginUsername.text.toString().isNotEmpty()
                && binding.loginPassword.text.toString().isNotEmpty()
            ) {
                signIn(binding.loginUsername.text.toString(), binding.loginPassword.text.toString(), view)
            }
            else {
                Toast.makeText(view.context, "Enter emailId and password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.SignInToSignUp.setOnClickListener {
            view.findNavController().navigate(R.id.action_userSignInFragment_to_userSignUpFragment)
        }

        binding.signInForgotPassword.setOnClickListener {
            view.findNavController().navigate(R.id.action_userSignInFragment_to_forgotPasswordFragment)
        }

    }

    private fun checkIfUserSignedIn() : Boolean {
        val currentUser : FirebaseUser? = auth.currentUser
        if(currentUser != null)
            return true
        return false
    }

    private fun signIn(email : String, password : String, view : View) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(view.context, "SignIn Successful", Toast.LENGTH_SHORT).show()
                    view.findNavController()
                        .navigate(R.id.action_userSignInFragment_to_homeScreenFragment)

                } else {
                    Toast.makeText(view.context, "Incorrect credentials", Toast.LENGTH_SHORT).show()
                }
            }
    }
}