package com.example.moviebookingapp.ui

import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.moviebookingapp.R
import com.example.moviebookingapp.databinding.FragmentUserSignUpBinding
import com.example.moviebookingapp.utils.NetworkUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserSignUpView {

    private lateinit var fname : String
    private lateinit var email : String
    private lateinit var mobile : String
    private lateinit var password : String
    private lateinit var c_password : String

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var reference : DatabaseReference

    fun workWithListener(view : View, binding : FragmentUserSignUpBinding) {

        reference = FirebaseDatabase.getInstance().getReference("Users")

        binding.signUpMobileNo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length != 10) {
                    binding.signUpMobileNo.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.va_baseline_smartphone_24,
                        0,
                        R.drawable.va_baseline_error_outline_24,
                        0
                    )
                } else {
                    binding.signUpMobileNo.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.va_baseline_smartphone_24,
                        0,
                        0,
                        0
                    )
                }
            }
        })

        var flag = true
        var confirmFlag = true

        binding.signUpShowPassword1.setImageResource(R.drawable.va_baseline_visibility_off_24)
        binding.signUpShowPassword1.setOnClickListener {
            if (flag) {
                binding.signUpShowPassword1.setImageResource(R.drawable.va_baseline_visibility_24)
                binding.signUpPassword1.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                flag = false
            } else {
                binding.signUpShowPassword1.setImageResource(R.drawable.va_baseline_visibility_off_24)
                binding.signUpPassword1.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                flag = true
            }
        }

        binding.signUpShowPassword2.setImageResource(R.drawable.va_baseline_visibility_off_24)
        binding.signUpShowPassword2.setOnClickListener {
            if (confirmFlag) {
                binding.signUpShowPassword2.setImageResource(R.drawable.va_baseline_visibility_24)
                binding.signUpPassword2.transformationMethod = HideReturnsTransformationMethod.getInstance()
                confirmFlag = false
            } else {
                binding.signUpShowPassword2.setImageResource(R.drawable.va_baseline_visibility_off_24)
                binding.signUpPassword2.transformationMethod = PasswordTransformationMethod.getInstance()
                confirmFlag = true
            }
        }

        binding.signUpButton.setOnClickListener {

            fname = binding.signUpName.text.toString()
            email = binding.signUpEmailId.text.toString()
            mobile = binding.signUpMobileNo.text.toString()
            password = binding.signUpPassword1.text.toString()
            c_password = binding.signUpPassword2.text.toString()

            if(!NetworkUtils.isInternetAvailable(view.context)) {
                view.findNavController().navigate(R.id.action_userSignUpFragment_to_noInternetFragment)
            }
            else if(fname.isEmpty())
                binding.signUpName.error = "This Field cannot be empty"

            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                binding.signUpEmailId.error = "Invalid Email"

            else if(mobile.length != 10)
                binding.signUpMobileNo.error = "Phone Number Should be of 10 digits"

            else if(password.length < 8)
                Toast.makeText(view.context, "Password should be minimum of 8 characters", Toast.LENGTH_SHORT).show()

            else if(c_password.length < 8)
                Toast.makeText(view.context, "Password should be minimum of 8 characters", Toast.LENGTH_SHORT).show()

            else if(password != c_password)
                Toast.makeText(view.context, "Passwords does not match", Toast.LENGTH_SHORT).show()

            else
                signUp(view)
        }

        binding.SignUpToSignIn.setOnClickListener {
            view.findNavController().navigate(R.id.action_userSignUpFragment_to_userSignInFragment)
        }
    }

    private fun signUp(view : View ) {
        auth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Toast.makeText(view.context, "SignUp Successful", Toast.LENGTH_SHORT).show()
                    val key : String = auth.uid!!
                    reference.child(key).child("Name").setValue(fname)
                    reference.child(key).child("Email Id").setValue(email)
                    reference.child(key).child("Mobile Number").setValue(password)
                    view.findNavController().navigate(R.id.action_userSignUpFragment_to_homeScreenFragment)
                }
                else {
                    Toast.makeText(view.context, "Sign Up Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

}