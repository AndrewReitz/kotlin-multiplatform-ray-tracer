package cash.andrew.mntrailconditions.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.util.ComponentContainer
import cash.andrew.mntrailconditions.util.makeComponent
import com.kobakei.ratethisapp.RateThisApp
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ComponentContainer<ActivityComponent> {

    private inline val bottomNavView get() = bottom_nav_view

    @Inject lateinit var viewContainer: ViewContainer

    override val component: ActivityComponent by lazy { makeComponent() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = layoutInflater

        component.inject(this)

        val container = viewContainer.forActivity(this)
        inflater.inflate(R.layout.main_activity, container)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        NavigationUI.setupWithNavController(bottomNavView, navHostFragment.navController)

        RateThisApp.onCreate(this)
        RateThisApp.showRateDialogIfNeeded(this)
    }
}
