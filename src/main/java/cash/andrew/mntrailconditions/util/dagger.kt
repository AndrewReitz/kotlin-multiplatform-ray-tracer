package cash.andrew.mntrailconditions.util

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import cash.andrew.mntrailconditions.MnTrailConditionsApp
import cash.andrew.mntrailconditions.ui.ActivityComponent

/**
 * Interface to express that an object is the owner of a component. You can get a reference to this
 * component to build a subcomponent.
 *
 * The component method should always return the same component. Typical practice is to store the
 * component as a private field. In your `component` implementation, check if the field is null, and
 * if so, instantiate the component object.
 */
interface ComponentContainer<out T> {
    val component: T
}

/**
 * Creates a new [ActivityComponent] for an activity.
 */
fun Activity.makeComponent(): ActivityComponent =
        (application as MnTrailConditionsApp)
                .component
                .activityComponentBuilder
                .activity(this)
                .build()

@Suppress("UNCHECKED_CAST")
val Fragment.component: ActivityComponent
    get() = (activity as ComponentContainer<ActivityComponent>).component

@Suppress("UNCHECKED_CAST")
val View.component: ActivityComponent
    get() = (context as ComponentContainer<ActivityComponent>).component
