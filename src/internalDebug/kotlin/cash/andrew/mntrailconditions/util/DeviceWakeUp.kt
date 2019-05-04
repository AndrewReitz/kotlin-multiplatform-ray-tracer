package cash.andrew.mntrailconditions.util

import android.app.Activity
import android.content.Context
import android.os.PowerManager
import android.view.WindowManager
import javax.inject.Inject

class DeviceWakeUp @Inject constructor(
        private val activity: Activity
) {
    /**
     * Show the activity over the lock-screen and wake up the device. If you launched the app manually
     * both of these conditions are already true. If you deployed from the IDE, however, this will
     * save you from hundreds of power button presses and pattern swiping per day!
     */
    @Suppress("DEPRECATION")
    fun riseAndShine() {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)

        val power = activity.getSystemService(Context.POWER_SERVICE) as PowerManager
        val lock = power.newWakeLock(PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE, "cash.andrew.mntrailconditions:mywakelock")
        lock.acquire()
        lock.release()
    }
}
