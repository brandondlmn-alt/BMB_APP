package com.example.bmbautos

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat

object NotificationHelper {

    // 1. Show a beautiful success popup
    fun showSuccessDialog(context: Context, title: String, message: String, onDismiss: (() -> Unit)? = null) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setIcon(android.R.drawable.checkbox_on_background)
            .setPositiveButton("Awesome!") { dialog, _ ->
                dialog.dismiss()
                onDismiss?.invoke()
            }
            .setCancelable(false)
            .show()
    }

    // 2. Trigger a System Tray Push Notification
    fun showSystemNotification(context: Context, title: String, message: String) {
        val channelId = "bmb_notifications"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "BMB Autos Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // Uses your app logo
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // Show it
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}