package cash.andrew.mntrailconditions.ui

import android.app.Activity
import android.view.ViewGroup

/** An indirection which allows controlling the root container used for each activity.  */
interface ViewContainer {
    /** The root [android.view.ViewGroup] into which the activity should place its contents.  */
    fun forActivity(activity: Activity): ViewGroup

    companion object {
        /** An [ViewContainer] which returns the normal activity content view.  */
        @SuppressWarnings("unused")
        val DEFAULT = object: ViewContainer {
            override fun forActivity(activity: Activity): ViewGroup = activity.findViewById(android.R.id.content)
        }
    }
}
