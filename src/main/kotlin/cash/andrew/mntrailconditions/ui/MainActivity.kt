package cash.andrew.mntrailconditions.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.util.ComponentContainer
import cash.andrew.mntrailconditions.util.DeviceWakeUp
import cash.andrew.mntrailconditions.util.makeComponent
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ComponentContainer<ActivityComponent> {

    private inline val bottomNavView get() = bottom_nav_view

    @Inject lateinit var deviceWakeUp: DeviceWakeUp

    override val component: ActivityComponent by lazy { makeComponent() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        component.inject(this)
        deviceWakeUp.riseAndShine()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        bottomNavView.setupWithNavController(navHostFragment.navController)
    }
}
