package com.example.moviebookingapp.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebookingapp.R
import com.example.moviebookingapp.data.Constants

class TimingsListAdapter(arrayList: List<String>, theatreName : String, context: Context) :
    RecyclerView.Adapter<TimingsListAdapter.RecyclerViewHolder>() {

    private val theatreTimingModelList: List<String>
    private val context: Context
    private val theatreName : String

    init {
        theatreTimingModelList = arrayList
        this.context = context
        this.theatreName = theatreName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_theatre_time_layout, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return theatreTimingModelList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.theatreTiming.text = theatreTimingModelList[position]

        holder.theatreTiming.setOnClickListener {
            holder.onCLick()
        }
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val theatreTiming: TextView = itemView.findViewById(R.id.theatre_timing)

        fun onCLick() {
            Constants.movieTiming = theatreTiming.text.toString()
            Constants.theatreName = theatreName
            itemView.findNavController().navigate(R.id.action_theatreSelectionFragment_to_seatSelectionFragment)
        }
    }
}