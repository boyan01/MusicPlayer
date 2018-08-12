package tech.soit.quiet.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import tech.soit.quiet.R
import tech.soit.quiet.ui.activity.base.BaseActivity

/**
 * the main activity of application
 */
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {

        private const val TAG_FRAGMENT_NETEASE = "netease_main"
        private const val TAG_FRAGMENT_LOCAL = "local_main"

    }

    private var checkedFragment = TAG_FRAGMENT_NETEASE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()
        checkToFragment(checkedFragment)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

        }
        return false
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun checkToFragment(tag: String) {

    }

    private var toggle: ActionBarDrawerToggle? = null

    private fun getToggle(toolbar: Toolbar?): ActionBarDrawerToggle {
        this.toggle?.let {
            drawerLayout.removeDrawerListener(it)
        }
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer)
        drawerLayout.addDrawerListener(toggle)
        this.toggle = toggle
        return toggle
    }

    private fun Fragment.toolbar(): Toolbar? = when (this) {
        else -> null
    }

}
