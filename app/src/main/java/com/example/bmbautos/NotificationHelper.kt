package com.example.bmbautos

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat

object NotificationHelper {

    // 1. Show a beautiful success popup dialog
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

    // 2. Trigger System Tray Push Notification
    fun showNotification(context: Context, title: String, message: String) {
        val channelId = "bmb_alerts_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Notification Channel for Android 8.0+ (API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "BMB Autos Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Build notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    // Alias for backward compatibility
    fun showSystemNotification(context: Context, title: String, message: String) {
        showNotification(context, title, message)
    }
}
