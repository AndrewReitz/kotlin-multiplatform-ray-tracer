package cash.andrew.mntrailconditions.util

import android.app.Activity
import android.content.Context
import android.os.PowerManager
import android.view.WindowManager
import javax.inject.Inject

/** Do nothing in release mode. */
class DeviceWakeUp @Inject constructor(
        private val activity: Activity
) {
    /**
     * Show the activity over the lock-screen and wake up the device. If you launched the app manually
     * both of these conditions are already true. If you deployed from the IDE, however, this will
     * save you from hundreds of power button presses and pattern swiping per day!
     */
    fun riseAndShine() = Unit
}
