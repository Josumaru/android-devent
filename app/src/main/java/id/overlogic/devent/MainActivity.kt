package id.overlogic.devent

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import id.overlogic.devent.databinding.ActivityMainBinding
import id.overlogic.devent.ui.finished.FinishedFragment
import id.overlogic.devent.ui.home.HomeFragment
import id.overlogic.devent.ui.upcoming.UpcomingFragment

class MainActivity : AppCompatActivity() ,  NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fragment: Fragment
    private lateinit var bottomNavigation: BottomNavigationView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bottomNavigation = binding.bottomNavigation
        bottomNavigation.selectedItemId = R.id.nav_home
        bottomNavigation.setOnItemSelectedListener(this)

        fragment = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId: Int = item.itemId

        fragment = when (itemId) {
            R.id.nav_home -> {
                HomeFragment()
            }

            R.id.nav_upcoming -> {
                UpcomingFragment()
            }

            R.id.nav_finished -> {
                FinishedFragment()
            }

            else -> return false
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        return true
    }

}