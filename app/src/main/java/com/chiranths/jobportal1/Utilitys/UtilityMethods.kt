package com.chiranths.jobportal1.Utilitys

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object UtilityMethods {


    fun showToast(context: Context, message:String){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }

    fun getCurrentTimeDate() :String? {
        val calendar: Calendar = Calendar.getInstance()
        val currentDate: Date = calendar.getTime()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate: String = dateFormat.format(currentDate)
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        val formattedTime: String = timeFormat.format(currentDate)
         return formattedDate+formattedTime
    }

    fun getTime(): String? {
        val calendar: Calendar = Calendar.getInstance()
        val currentDate: Date = calendar.getTime()
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        val formattedTime: String = timeFormat.format(currentDate)
        return formattedTime
    }
    fun getDate(): String? {
        val calendar: Calendar = Calendar.getInstance()
        val currentDate: Date = calendar.getTime()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate: String = dateFormat.format(currentDate)
        return formattedDate
    }
}