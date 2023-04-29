package com.example.moviebookingapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebookingapp.databinding.CustomTicketsPurchaseHistoryBinding
import com.example.moviebookingapp.model.TicketsPurchased
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class PurchaseHistoryAdapter :
    ListAdapter<TicketsPurchased, PurchaseHistoryAdapter.TicketHistoryViewHolder>(ComparatorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketHistoryViewHolder {
        Log.d("Application","Inside List Adapter")
        val binding = CustomTicketsPurchaseHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketHistoryViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(it)
        }
    }

    inner class TicketHistoryViewHolder(private val binding: CustomTicketsPurchaseHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(ticket: TicketsPurchased) {

            if(ticket.type == "event") {

                binding.confirmationId.text = "ID : " + ticket.transactionID
                binding.confirmationName.text = "Event Name : " + ticket.name
                binding.confirmationDate.text = "Event Date : " + ticket.date
                binding.confirmationLocation.text = "Event Location : " + ticket.location
                binding.confirmationSeat.text = "No. of Seats : " + ticket.seatSelected

                val qrText: String =
                    "Booking Id : " + ticket.transactionID +
                            "\nEvent Name : " + ticket.name +
                            "\nEvent Date : " + ticket.date +
                            "\nEvent Location : " + ticket.location +
                            "\nSeats Booked : " + ticket.seatSelected

                generateQR(qrText)
            }
            else {
                binding.confirmationId.text = "Id : " + ticket.transactionID
                binding.confirmationName.text = "Movie Name : " + ticket.name
                binding.confirmationDate.text = "Date : " + ticket.date
                binding.confirmationLocation.text = "Theatre : " + ticket.location
                binding.confirmationSeat.text = "Seat Selected : " + ticket.seatSelected

                val qrText: String =
                    "Booking Id : " + ticket.transactionID +
                            "\nMovie Name : " + ticket.name +
                            "\nTheatre : " + ticket.location +
                            "\nDate : " + ticket.date +
                            "\nSeat Selected : " + ticket.seatSelected

                generateQR(qrText)
            }
        }

        private fun generateQR(qrText : String) {
            val multiFormatWriter = MultiFormatWriter()
            try {
                val matrix: BitMatrix = multiFormatWriter.encode(qrText, BarcodeFormat.QR_CODE, 600, 600)
                val encoder = BarcodeEncoder()
                val bitmap = encoder.createBitmap(matrix)
                //set data image to imageview
                binding.confirmationQrCode.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<TicketsPurchased>() {
        override fun areItemsTheSame(oldItem: TicketsPurchased, newItem: TicketsPurchased): Boolean {
            return oldItem.transactionID == newItem.transactionID
        }

        override fun areContentsTheSame(oldItem: TicketsPurchased, newItem: TicketsPurchased): Boolean {
            return oldItem == newItem
        }
    }
}