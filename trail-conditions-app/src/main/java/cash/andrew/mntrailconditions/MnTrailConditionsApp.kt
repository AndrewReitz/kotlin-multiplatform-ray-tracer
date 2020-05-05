package cash.andrew.mntrailconditions

import android.app.Application
import cash.andrew.mntrailconditions.util.ComponentContainer
import com.jakewharton.threetenabp.AndroidThreeTen
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import cash.andrew.mntrailconditions.ui.ActivityComponent
import cash.andrew.mntrailconditions.util.StartUpFirebaseTopicSubscriber

typealias MnTrailConditionsInitializer = ((Application) -> Unit)

class MnTrailConditionsApp : Application(), ComponentContainer<AppComponent> {

    private val appInitializer by lazy { component.appInitializer }
    private val startUpFirebaseTopicSubscriber by lazy { component.startUpFirebaseTopicSubscriber }

    private lateinit var _component: AppComponent
    override val component by lazy { _component }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        _component = AppComponent.builder()
                .application(this)
                .build()

        appInitializer(this)
        startUpFirebaseTopicSubscriber.subscribe()

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val name = getString(R.string.notification_channel_trail_status)
        val description = getString(R.string.notification_channel_trail_status_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(name, name, importance)
        channel.description = description

        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = getSystemService(NotificationManager::class.java)!!
        notificationManager.createNotificationChannel(channel)
    }
}

/** Component that both production and internal builds dagger graphs share. */
interface BaseComponent {
    val startUpFirebaseTopicSubscriber: StartUpFirebaseTopicSubscriber
    val appInitializer: MnTrailConditionsInitializer
    val activityComponentBuilder: ActivityComponent.Builder
}
