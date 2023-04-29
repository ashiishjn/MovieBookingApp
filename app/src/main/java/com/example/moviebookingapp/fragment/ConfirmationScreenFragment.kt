package com.example.moviebookingapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.moviebookingapp.R
import com.example.moviebookingapp.data.Constants
import com.example.moviebookingapp.databinding.FragmentConfirmationScreenBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class ConfirmationScreenFragment : Fragment() {

    private var _binding : FragmentConfirmationScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmationScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.confirmationMovieName.text = "Movie Name : " + Constants.movieName
        binding.confirmationMovieTheatre.text ="Theatre : " + Constants.theatreName
        binding.confirmationMovieDate.text = "Date : " + Constants.movieDate
        binding.confirmationMovieTime.text = "Time : " + Constants.movieTiming
        binding.confirmationMovieSeat.text = "Seat Selected : " + Constants.movieSeatsSelected
        binding.confirmationId.text = "Transaction ID : " + Constants.movieTicketTransactionID

        generateQR(binding)

        binding.confirmationHomeScreenButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_confirmationScreenFragment_to_homeScreenFragment)
        }
    }

    fun generateQR(binding : FragmentConfirmationScreenBinding) {

        val qr_text: String = binding.confirmationId.text.toString() +
                "\nMovie Name : " + Constants.movieName +
                "\nTheatre : " + Constants.theatreName +
                "\nDate : " + Constants.movieDate +
                "\nTime : " + Constants.movieTiming +
                "\nSeat Selected : " + Constants.movieSeatsSelected

        val multiFormatWriter: MultiFormatWriter = MultiFormatWriter()
        try {
            val matrix: BitMatrix =
                multiFormatWriter.encode(qr_text, BarcodeFormat.QR_CODE, 600, 600)
            val encoder = BarcodeEncoder()
            val bitmap = encoder.createBitmap(matrix)
            //set data image to imageview
            binding.confirmationQrCode.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}