package cash.andrew.mntrailconditions.messaging

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class TrailMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        Timber.i("Message received! $message")
    }

    override fun onDeletedMessages() {
    }
}
