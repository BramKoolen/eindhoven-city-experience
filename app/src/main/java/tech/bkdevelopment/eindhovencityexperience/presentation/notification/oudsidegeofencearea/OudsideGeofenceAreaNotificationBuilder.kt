package tech.bkdevelopment.eindhovencityexperience.presentation.notification.oudsidegeofencearea

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import tech.bkdevelopment.eindhovencityexperience.R
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourdetail.TourDetailActivity
import javax.inject.Inject


class OudsideGeofenceAreaNotificationBuilder @Inject constructor() {

    private var notificationManager: NotificationManager? = null

    fun createNotification(context: Context, tourId: String) {

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            TourDetailActivity.createIntent(context, tourId, true),
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((notificationManager?.activeNotifications?.firstOrNull { it.id == NOTIFICATION_ID }) == null) {
                createNotificationChannel(context)

                val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(context.getString(R.string.oudside_geofence_area_notification_title))
                    .setContentText(context.getString(R.string.oudside_geofence_area_notification_body))
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)

                notificationManager?.notify(NOTIFICATION_ID, builder.build())
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.oudside_geofence_area_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description =
                    context.getString(R.string.oudside_geofence_area_notification_channel_description)
            }

            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }

    companion object {

        private const val CHANNEL_ID = "oudsideId"
        private const val NOTIFICATION_ID = 678
    }
}