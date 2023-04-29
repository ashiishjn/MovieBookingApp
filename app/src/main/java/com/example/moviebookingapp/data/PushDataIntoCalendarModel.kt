package com.example.moviebookingapp.data

import com.example.moviebookingapp.model.CustomCalendar
import java.util.*
import kotlin.collections.ArrayList

class PushDataIntoCalendarModel {

    fun pushData() : ArrayList<CustomCalendar> {
        val cal = Calendar.getInstance()

        val calendarArrayList : ArrayList<CustomCalendar> = ArrayList()

        for (i in 1..7){
            calendarArrayList.add(CustomCalendar(geDay(cal[Calendar.DAY_OF_WEEK]),
                cal[Calendar.DATE].toString(), geMonth(cal[Calendar.MONTH])))
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendarArrayList
    }

    private fun geDay(day: Int) : String{
        return when (day) {
            1 -> "SUN"
            2 -> "MON"
            3 -> "TUE"
            4 -> "WED"
            5 -> "THU"
            6 -> "FRI"
            else -> "SAT"
        }
    }

    private fun geMonth(month: Int) : String{
        return when (month) {
            0 -> "JAN"
            1 -> "FEB"
            2 -> "MAR"
            3 -> "APR"
            4 -> "MAY"
            5 -> "JUN"
            6 -> "JUL"
            7 -> "AUG"
            8 -> "SEP"
            9 -> "OCT"
            10 -> "NOV"
            else -> "DEC"
        }
    }
}