package th.ac.kku.coe.swabook

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)

        findViewById<BottomNavigationView>(R.id.bottom_nav).setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, _, _ ->
            controller.currentDestination.let {
                when (it?.id) {
                    R.id.splashFragment -> {
                        hideBottomNav()
                    }

                    R.id.loginPageFragment -> {
                        toolbar.visibility = View.GONE
                        hideBottomNav()
                    }

                    R.id.registerFragment -> {
                        hideBottomNav()
                    }
                    R.id.resetPasswordFragment -> {
                        hideBottomNav()
                    }

                    R.id.bookFragment -> {
                        toolbar.visibility = View.VISIBLE
                        hideBottomNav()
                    }
                    R.id.feedFragment -> {
                        toolbar.visibility = View.GONE
                        showBottomNav()
                    }
                    R.id.ChatFragment -> {
                        toolbar.visibility = View.GONE
                        hideBottomNav()
                    }
                    R.id.myBookFragment, R.id.myBookDetailFragment, R.id.myBookEditFragment -> {
                        toolbar.visibility = View.VISIBLE
                        hideBottomNav()

                        /*  } R.id.myBookDetailFragment -> {
                              toolbar.visibility = View.VISIBLE
                              hideBottomNav()
                          }
                          R.id.myBookEditFragment -> {
                              toolbar.visibility = View.VISIBLE
                              hideBottomNav()*/
                    }
                    else -> {
                        toolbar.visibility = View.GONE
                        showBottomNav()
                    }
                }
            }
        }
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    private fun showBottomNav() {
        bottom_nav.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        bottom_nav.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return navController.navigateUp()
    }

}
