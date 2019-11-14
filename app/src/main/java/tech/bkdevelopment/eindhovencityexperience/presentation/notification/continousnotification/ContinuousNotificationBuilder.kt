package tech.bkdevelopment.eindhovencityexperience.presentation.notification.continousnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import tech.bkdevelopment.eindhovencityexperience.R
import tech.bkdevelopment.eindhovencityexperience.generic.extension.getBitmapFromVectorDrawable
import javax.inject.Inject

class ContinuousNotificationBuilder @Inject constructor() {

    private var notificationManager: NotificationManager? = null

    fun createNotification(
        context: Context,
        continuousNotification: ContinuousNotification
    ): Notification {

        createNotificationChannel(context)

        val largeIcon = if (continuousNotification.isUnlocked) {
            context.getDrawable(R.drawable.ic_lock_open_green)
        } else {
            context.getDrawable(R.drawable.ic_lock_outline_black)
        }

        return NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setContentTitle(continuousNotification.title)
            .setContentText(continuousNotification.body)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(largeIcon?.let { context.getBitmapFromVectorDrawable(it) })
            .setOngoing(true)
            //.setContentIntent(pendingIntent) TODO navigate to map or story
            .build()
    }

    private fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.continuous_notification_channel_name)
            val descriptionText =
                context.getString(R.string.continuous_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun updateNotification(
        context: Context,
        notificationID: Int,
        continuousNotification: ContinuousNotification
    ) {
        notificationManager?.notify(
            notificationID,
            createNotification(context, continuousNotification)
        )
    }

    companion object {

        private const val CHANNEL_ID = "continuousId"
    }
}