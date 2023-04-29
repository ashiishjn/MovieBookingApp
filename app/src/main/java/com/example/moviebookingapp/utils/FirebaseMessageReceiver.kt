package com.example.moviebookingapp.utils

import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.example.moviebookingapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseMessageReceiver : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if(message.notification != null) {
            message.notification!!.title?.let {
                message.notification!!.body?.let {
                        it1 ->
                showNotification(it, it1)
                }
            }
        }
    }

//    private fun getCustomDesign(
//        title: String,
//        message: String
//    ): RemoteViews {
//        val remoteViews = RemoteViews(
//            MainApplication.applicationContext().getPackageName(),
//            R.layout.notification
//        )
//        remoteViews.setTextViewText(R.id.title, title)
//        remoteViews.setTextViewText(R.id.message, message)
//        remoteViews.setImageViewResource(
//            R.id.icon,
//            R.drawable.logo
//        )
//        return remoteViews
//    }

    private fun showNotification(title : String, message : String) {

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, com.example.moviebookingapp.data.Constants.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigTextStyle())
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(com.example.moviebookingapp.data.Constants.NOTIFICATION_ID, notificationBuilder.build())

//        val intent = Intent(this, MainActivity::class.java)
//        // Assign channel ID
//        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
//        // the activities present in the activity stack,
//        // on the top of the Activity that is to be launched
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        // Pass the intent to PendingIntent to start the
//        // next Activity
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )
//
//        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
//            MainApplication.applicationContext(),
//            Constants.CHANNEL_ID
//        )
//            .setSmallIcon(R.drawable.logo)
//            .setAutoCancel(true)
//            .setVibrate(
//                longArrayOf(
//                    1000, 1000, 1000,
//                    1000, 1000
//                )
//            )
//            .setOnlyAlertOnce(true)
//            .setContentIntent(pendingIntent)
//
//        builder = builder.setContent( getCustomDesign(title, message) )
//
//        val notificationManager = ContextCompat.getSystemService(
//
//            Context.NOTIFICATION_SERVICE
//        ) as NotificationManager?
//        // Check if the Android Version is greater than Oreo
//        if (Build.VERSION.SDK_INT
//            >= Build.VERSION_CODES.O
//        ) {
//            val notificationChannel = NotificationChannel(
//                Constants.CHANNEL_ID, "web_app",
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            notificationManager!!.createNotificationChannel(
//                notificationChannel
//            )
//        }
//        notificationManager!!.notify(0, builder.build())
    }
}
