package cash.andrew.mntrailconditions.ui.debug

import android.content.Context.POWER_SERVICE
import android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP
import android.os.PowerManager.FULL_WAKE_LOCK
import android.os.PowerManager.ON_AFTER_RELEASE
import android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
import java.util.concurrent.TimeUnit.SECONDS

import android.app.Activity
import android.os.PowerManager
import android.support.v4.view.GravityCompat
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.SeenDebugDrawer
import cash.andrew.mntrailconditions.ui.ViewContainer

import com.f2prateek.rx.preferences2.Preference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * An [ViewContainer] for debug builds which wraps a sliding drawer on the right that holds
 * all of the debug information and settings.
 */
@Singleton
class DebugViewContainer @Inject
constructor(
        @param:SeenDebugDrawer private val seenDebugDrawer: Preference<Boolean>
) : ViewContainer {

    class ViewHolder {
        lateinit var drawerLayout: DebugDrawerLayout
        lateinit var debugDrawer: ViewGroup
        lateinit var content: FrameLayout
    }

    override fun forActivity(activity: Activity): ViewGroup {
        activity.setContentView(R.layout.debug_activity_frame)

        val viewHolder = ViewHolder().apply {
            drawerLayout = activity.findViewById(R.id.debug_drawer_layout)
            debugDrawer = activity.findViewById(R.id.debug_drawer)
            content = activity.findViewById(R.id.debug_content)
        }

        val drawerContext = ContextThemeWrapper(activity, R.style.Theme_MnTrailConditions_Debug)
        val debugView = DebugView(drawerContext)
        viewHolder.debugDrawer.addView(debugView)

        viewHolder.drawerLayout.setDrawerShadow(R.drawable.debug_drawer_shadow, GravityCompat.END)
        viewHolder.drawerLayout.setDrawerListener(
                object : DebugDrawerLayout.SimpleDrawerListener() {
                    override fun onDrawerOpened(drawerView: View) {
                        debugView.onDrawerOpened()
                    }
                })

        // If you have not seen the debug drawer before, show it with a message
        if (!seenDebugDrawer.get()) {
            viewHolder.drawerLayout.postDelayed(
                    {
                        viewHolder.drawerLayout.openDrawer(GravityCompat.END)
                        Toast.makeText(drawerContext, R.string.debug_drawer_welcome, Toast.LENGTH_LONG).show()
                    },
                    SECONDS.toMillis(1))
            seenDebugDrawer.set(true)
        }

        riseAndShine(activity)
        return viewHolder.content
    }

    companion object {

        /**
         * Show the activity over the lock-screen and wake up the device. If you launched the app manually
         * both of these conditions are already true. If you deployed from the IDE, however, this will
         * save you from hundreds of power button presses and pattern swiping per day!
         */
        fun riseAndShine(activity: Activity) {
            activity.window.addFlags(FLAG_SHOW_WHEN_LOCKED)

            val power = activity.getSystemService(POWER_SERVICE) as PowerManager
            val lock = power.newWakeLock(FULL_WAKE_LOCK or ACQUIRE_CAUSES_WAKEUP or ON_AFTER_RELEASE, "wakeup!")
            lock.acquire()
            lock.release()
        }
    }
}
