package com.example.moviebookingapp.utils

import android.Manifest
import android.content.pm.PackageManager
import android.text.Html
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.moviebookingapp.R
import com.example.moviebookingapp.data.Constants

class NotificationBuilder {

    suspend fun notificationBuilder(notificationDetails : String, view: View) {
        val builder = NotificationCompat.Builder(view.context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Congrats! Your booking is successful.")
//            .setContentText(notification_details + "\nSeats Selected : " + seatDetails)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(notificationDetails))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManagerCompat : NotificationManagerCompat = NotificationManagerCompat.from(view.context)
        if (ActivityCompat.checkSelfPermission(
                view.context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManagerCompat.notify(Constants.NOTIFICATION_ID, builder.build())
    }
}