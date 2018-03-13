package cash.andrew.mntrailconditions

import android.app.Application
import cash.andrew.mntrailconditions.util.HasComponent
import com.jakewharton.processphoenix.ProcessPhoenix
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary

import org.threeten.bp.format.DateTimeFormatter

import timber.log.Timber

typealias MnTrailConditionsInitializer = ((Application) -> Unit)

class MnTrailConditionsApp : Application(), HasComponent<AppComponent> {

    val activityHierarchyServer by lazy { component.activityHierarchyServer }
    val lumberYard by lazy { component.lumberYard }
    val appInitializer by lazy { component.appInitializer }

    private lateinit var _component: AppComponent
    override val component by lazy { _component }

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this) || ProcessPhoenix.isPhoenixProcess(this)) {
            return
        }

        LeakCanary.install(this)
        AndroidThreeTen.init(this)

        _component = DaggerAppComponent.builder()
                .application(this)
                .build()

        appInitializer(this)

        lumberYard.cleanUp()
        Timber.plant(lumberYard.tree())

        registerActivityLifecycleCallbacks(activityHierarchyServer)
    }

    companion object {
        val DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a")!!
    }
}
