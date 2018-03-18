package cash.andrew.mntrailconditions.ui

import android.app.Activity
import android.app.Application
import android.os.Bundle

/** A "view server" adaptation which automatically hooks itself up to all activities.  */
interface ActivityHierarchyServer : Application.ActivityLifecycleCallbacks {
    companion object {
        /** An [ActivityHierarchyServer] which does nothing.  */
        val NONE: ActivityHierarchyServer = object : ActivityHierarchyServer {
            override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {}

            override fun onActivityDestroyed(activity: Activity) {}
        }
    }
}
