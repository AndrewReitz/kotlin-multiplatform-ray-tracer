package cash.andrew.mntrailconditions.util

import android.app.Activity
import android.content.Context

/**
 * Interface to express that an object is the owner of a component. You can get a reference to this
 * component to build a subcomponent.
 *
 *
 * The component method should always return the same component. Typical practice is to store the
 * component as a private field. In your `component` implementation, check if the field is null, and
 * if so, instantiate the component object.
 */
interface HasComponent<out T> {
    val component: T
}

//fun <T> T.makeComponent():
//        ActivityComponent where T : HasComponent<ActivityComponent>,
//                                T : Activity = (application as PrideApp)
//        .component
//        .activityComponentBuilder()
//        .activity(this)
//        .build()
//
//@Suppress("UNCHECKED_CAST")
//val Context.activityComponent
//    get() = (this as HasComponent<ActivityComponent>).component
