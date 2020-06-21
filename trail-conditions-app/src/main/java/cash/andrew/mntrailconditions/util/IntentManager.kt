package cash.andrew.mntrailconditions.util

import android.widget.Toast.LENGTH_LONG

import android.app.Application
import android.content.Intent
import android.widget.Toast
import cash.andrew.mntrailconditions.R
import dagger.Reusable
import javax.inject.Inject

@Reusable
class IntentManager @Inject constructor(private val context: Application) {

    /**
     * Attempt to launch the supplied [Intent]. Queries on-device packages before launching and
     * will display a simple message if none are available to handle it.
     */
    fun maybeStartActivity(intent: Intent): Boolean {
        return maybeStartActivity(intent, false)
    }

    /**
     * Attempt to launch Android's chooser for the supplied [Intent]. Queries on-device packages
     * before launching and will display a simple message if none are available to handle it.
     */
    fun maybeStartChooser(intent: Intent): Boolean {
        return maybeStartActivity(intent, true)
    }

    private fun maybeStartActivity(intent: Intent, chooser: Boolean): Boolean {
        var target = intent
        return if (hasHandler(target)) {
            if (chooser) {
                target = Intent.createChooser(target, null)
            }
            target.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(target)
            true
        } else {
            Toast.makeText(context, R.string.no_intent_handler, LENGTH_LONG).show()
            false
        }
    }

    /** Queries on-device packages for a handler for the supplied [Intent].  */
    private fun hasHandler(intent: Intent): Boolean {
        val handlers = context.packageManager.queryIntentActivities(intent, 0)
        return !handlers.isEmpty()
    }
}
