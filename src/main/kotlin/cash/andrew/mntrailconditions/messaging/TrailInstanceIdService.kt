package cash.andrew.mntrailconditions.messaging

import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.iid.FirebaseInstanceId
import timber.log.Timber

class TrailInstanceIdService: FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Timber.d("Refreshed token: %s", refreshedToken)
    }
}
