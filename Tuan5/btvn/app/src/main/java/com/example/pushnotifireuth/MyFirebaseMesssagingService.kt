package com.example.pushnotifireuth

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM", "From: ${remoteMessage.from}")

        // Check if message contains a notification payload
        remoteMessage.notification?.let {
            Log.d("FCM", "Notification Title: ${it.title}")
            Log.d("FCM", "Notification Body: ${it.body}")

            // Show notification based on type
            val type = remoteMessage.data["type"]
            when (type) {
                "profile" -> NotificationHelper.sendProfileNotification(this)
                else -> NotificationHelper.sendGeneralNotification(this)
            }
        }

        // Check if message contains a data payload
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FCM", "Data Payload: ${remoteMessage.data}")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New FCM Token: $token")

        // TODO: Send token to your server if needed
        // sendTokenToServer(token)
    }
}