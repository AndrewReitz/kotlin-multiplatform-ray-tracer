package cash.andrew.mntrailconditions.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.databinding.MainActivityBinding
import cash.andrew.mntrailconditions.util.ComponentContainer
import cash.andrew.mntrailconditions.util.DeviceWakeUp
import cash.andrew.mntrailconditions.util.makeComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ComponentContainer<ActivityComponent> {

    @Inject lateinit var deviceWakeUp: DeviceWakeUp

    override val component: ActivityComponent by lazy { makeComponent() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = MainActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        component.inject(this)
        deviceWakeUp.riseAndShine()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        binding.bottomNavView.setupWithNavController(navHostFragment.navController)
    }
}
