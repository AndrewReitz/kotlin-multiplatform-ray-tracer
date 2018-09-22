package cash.andrew.mntrailconditions

import android.app.Application
import android.os.Looper
import cash.andrew.mntrailconditions.util.ComponentContainer
import com.jakewharton.processphoenix.ProcessPhoenix
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

typealias MnTrailConditionsInitializer = ((Application) -> Unit)

class MnTrailConditionsApp : Application(), ComponentContainer<AppComponent> {

    private val lumberYard by lazy { component.lumberYard }
    private val appInitializer by lazy { component.appInitializer }

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

        RxJavaPlugins.setErrorHandler { Timber.e(it) }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler {
            AndroidSchedulers.from(Looper.getMainLooper(), true)
        }

        appInitializer(this)

        lumberYard.cleanUp()
        Timber.plant(lumberYard.tree())
    }
}
