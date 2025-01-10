package com.opbengalas.birthdayapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.opbengalas.birthdayapp.MainActivity
import com.opbengalas.birthdayapp.R
import com.opbengalas.birthdayapp.models.Contact
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NotificationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "birthday_channel",
                "Birthday Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for birthdays"
            }
            notificationManager.createNotificationChannel(channel)
            Log.d("NotificationService", "Notification channel created: ${channel.id}")
        }
    }

    fun showDefaultNotification(contact: Contact) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, "birthday_channel")
            .setSmallIcon(R.drawable.ic_birthday)
            .setContentTitle("🎉 ¡Feliz cumpleaños, ${contact.name}!")
            .setContentText("Hoy es el cumpleaños de ${contact.name}.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setNumber(1)

        notificationManager.notify(contact.id, builder.build())
    }

    fun showCustomNotification(contact: Contact) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, "birthday_channel")
            .setSmallIcon(R.drawable.ic_birthday)
            .setContentTitle("🎉 ¡Feliz cumpleaños, ${contact.name}!")
            .setContentText("Notificación personalizada para ${contact.name}.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        contact.notificationTone?.let { toneUri ->
            builder.setSound(Uri.parse(toneUri))
        }

        if (contact.notificationRepeat) {
            builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE)
        }

        notificationManager.notify(contact.id, builder.build())
    }
}
