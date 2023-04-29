package com.example.moviebookingapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebookingapp.R
import com.example.moviebookingapp.databinding.FragmentSeatSelectionBinding

class SeatLayoutAdapter(arrayList: ArrayList<String>, arrayListSeatSelected: ArrayList<Boolean>,
        mcontext: Context, binding : FragmentSeatSelectionBinding) :
                RecyclerView.Adapter<SeatLayoutAdapter.RecyclerViewHolder>()  {

    private val seatLayoutList: ArrayList<String>
    private val seatBookedList: ArrayList<Boolean>
    val seatSelectedList: ArrayList<String> = ArrayList()
    private val mcontext: Context
    private val binding : FragmentSeatSelectionBinding

    init {
        seatLayoutList = arrayList
        seatBookedList = arrayListSeatSelected
        this.mcontext = mcontext
        this.binding = binding
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_seat_layout, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return seatLayoutList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.seat.text = seatLayoutList[position]

        holder.seat.setOnClickListener {
            if(!seatBookedList[position]) {
                if (seatSelectedList.contains(holder.seat.text))
                    seatSelectedList.remove(holder.seat.text)
                else
                    seatSelectedList.add(holder.seat.text.toString())

                notifyDataSetChanged()
                setButtonVisibility()
            }
        }

        if (seatBookedList[position]) {
            holder.seat.setBackgroundResource(R.drawable.seat_booked)
            holder.seat.setTextColor(mcontext.getColor(R.color.white))
        }
        else if (seatSelectedList.contains(holder.seat.text)) {
            holder.seat.setBackgroundResource(R.drawable.seat_selected)
            holder.seat.setTextColor(mcontext.getColor(R.color.white))
        }
        else {
            holder.seat.setBackgroundResource(R.drawable.seat_available)
            holder.seat.setTextColor(mcontext.getColor(R.color.black))
        }
    }

    private fun setButtonVisibility() {
        if(seatSelectedList.size == 0)
            binding.purchaseTicketsButton.visibility = View.INVISIBLE
        else
            binding.purchaseTicketsButton.visibility = View.VISIBLE
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val seat: TextView = itemView.findViewById(R.id.seat_textview)

    }
}