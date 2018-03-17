package cash.andrew.mntrailconditions.ui

import butterknife.ButterKnife.findById

import android.app.Activity
import android.view.ViewGroup

/** An indirection which allows controlling the root container used for each activity.  */
interface ViewContainer {
    /** The root [android.view.ViewGroup] into which the activity should place its contents.  */
    fun forActivity(activity: Activity): ViewGroup

    companion object {
        /** An [ViewContainer] which returns the normal activity content view.  */
        val DEFAULT = object: ViewContainer {
            override fun forActivity(activity: Activity): ViewGroup =
                    findById<ViewGroup>(activity, android.R.id.content)
        }
    }
}
