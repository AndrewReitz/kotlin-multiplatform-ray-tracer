package cash.andrew.mntrailconditions.messaging

import android.app.Notification
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import androidx.core.app.NotificationManagerCompat
import cash.andrew.mntrailconditions.R

const val NOTIFICATION_ID = 1337

class TrailMessagingService : FirebaseMessagingService() {

    private val trailStatusChannel by lazy { getString(R.string.notification_channel_trail_status) }
    private val notificationManager by lazy { NotificationManagerCompat.from(applicationContext) }

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.run {
            NotificationCompat.Builder(this@TrailMessagingService, trailStatusChannel)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.ic_stat_morc)
                    .build()
                    .show(tag)
        }
    }

    private fun Notification.show(tag: String?) = notificationManager.notify(tag, NOTIFICATION_ID, this)
}
