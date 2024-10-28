package id.overlogic.devent

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import id.overlogic.devent.databinding.ActivityMainBinding
import id.overlogic.devent.ui.favourite.FavouriteFragment
import id.overlogic.devent.ui.finished.FinishedFragment
import id.overlogic.devent.ui.home.HomeFragment
import id.overlogic.devent.ui.settings.SettingsFragment
import id.overlogic.devent.ui.settings.SettingsViewModel
import id.overlogic.devent.ui.settings.ViewModelFactory
import id.overlogic.devent.ui.upcoming.UpcomingFragment
import id.overlogic.devent.util.SettingsPreferences
import id.overlogic.devent.util.dataStore

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigation: BottomNavigationView
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bottomNavigation = binding.bottomNavigation
        bottomNavigation.setOnItemSelectedListener(this)

        viewModel.selectedFragmentId.distinctUntilChanged().observe(this) { itemId ->
            val fragment = when (itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_upcoming -> UpcomingFragment()
                R.id.nav_finished -> FinishedFragment()
                R.id.nav_favourite -> FavouriteFragment()
                R.id.nav_settings -> SettingsFragment()
                else -> HomeFragment()
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .commit()
            bottomNavigation.selectedItemId = itemId
        }

        if (savedInstanceState == null) {
            viewModel.setSelectedFragmentId(R.id.nav_home)
        }

        val preferences = SettingsPreferences.getInstance(application.dataStore)
        val settingsViewModel = ViewModelProvider(this, ViewModelFactory(preferences)).get(
            SettingsViewModel::class.java
        )
        settingsViewModel.getThemeSettings().observe(this) { isDarkMode ->
            AppCompatDelegate.setDefaultNightMode(if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        viewModel.setSelectedFragmentId(item.itemId)
        return true
    }
}
