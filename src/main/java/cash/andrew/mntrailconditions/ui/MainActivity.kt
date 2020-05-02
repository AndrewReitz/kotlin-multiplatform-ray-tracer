package cash.andrew.mntrailconditions.ui

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
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

    @Inject
    lateinit var deviceWakeUp: DeviceWakeUp

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

        binding.overflowMenuButton.setOnClickListener { view ->
            PopupMenu(this, view).apply {
                menuInflater.inflate(R.menu.overflow_menu, menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_about -> {
                            navigateToMarkDownActivity(R.string.about, MarkDownFile.ABOUT)
                            true
                        }
                        R.id.menu_why_are_trails_closed -> {
                            navigateToMarkDownActivity(R.string.why_are_trails_closed, MarkDownFile.WHY_ARE_TRAILS_CLOSED)
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }
    }
}
