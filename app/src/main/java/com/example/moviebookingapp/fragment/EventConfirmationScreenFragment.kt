package com.example.moviebookingapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.moviebookingapp.R
import com.example.moviebookingapp.databinding.FragmentEventConfirmationScreenBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class EventConfirmationScreenFragment : Fragment() {

    private var _binding : FragmentEventConfirmationScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventConfirmationScreenBinding.inflate(inflater, container, false)
        return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setInitialData()

        binding.confirmationHomeScreenButton.setOnClickListener {
            findNavController().navigate(R.id.action_eventConfirmationScreenFragment_to_homeScreenFragment)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setInitialData() {
        val id = arguments?.getString("id")
        val name = arguments?.getString("name")
        val date = arguments?.getString("date")
        val location = arguments?.getString("location")
        val seats = arguments?.getString("seats")

        binding.confirmationId.text = "Booking Id : $id"
        binding.confirmationEventName.text = "Event Name : $name"
        binding.confirmationEventDate.text = "Event Date : $date"
        binding.confirmationEventLocation.text = "Event Location : $location"
        binding.confirmationEventSeat.text = "Seats Booked : $seats"

        val qr_text : String = "Booking Id : " + id +
                                "\nEvent Name : " + name +
                                "\nEvent Date : " + date +
                                "\nEvent Location : " + location +
                                "\nSeats Booked : " + seats

        generateQR(qr_text)
    }

    private fun generateQR(qr_text : String) {
        val multiFormatWriter : MultiFormatWriter = MultiFormatWriter()
        try {
            val matrix: BitMatrix = multiFormatWriter.encode(qr_text, BarcodeFormat.QR_CODE, 600, 600)
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