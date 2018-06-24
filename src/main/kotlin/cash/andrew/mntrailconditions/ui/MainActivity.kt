package cash.andrew.mntrailconditions.ui

import android.app.Activity
import android.os.Bundle
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.util.HasComponent
import cash.andrew.mntrailconditions.util.makeComponent
import com.kobakei.ratethisapp.RateThisApp
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : Activity(), HasComponent<ActivityComponent> {

    private val content get() = main_content

    @Inject lateinit var viewContainer: ViewContainer

    override val component: ActivityComponent by lazy { makeComponent() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = layoutInflater

        component.inject(this)

        val container = viewContainer.forActivity(this)

        inflater.inflate(R.layout.main_activity, container)
        inflater.inflate(R.layout.trail_list_view, content)

        RateThisApp.onCreate(this)
        RateThisApp.showRateDialogIfNeeded(this)
    }
}
