package tech.summerly.quiet

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.utils.intransaction
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.local.LocalMainFragment
import tech.summerly.quiet.netease.fragments.NeteaseMainFragment

/**
 * the main activity of application
 */
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {

        private const val TAG_FRAGMENT_NETEASE = "netease_main"
        private const val TAG_FRAGMENT_LOCAL = "local_main"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_netease)
    }

    override fun onStart() {
        super.onStart()
        navigationView.menu.findItem(R.id.nav_netease)?.actionView?.findViewById<View>(R.id.indicatorLayout)?.let {
            it.setOnClickListener {
                log { "netease user clicked" }
            }
        }
        navigationView.setCheckedItem(R.id.nav_netease)
        navigationView.menu.findItem(R.id.nav_netease)?.let {
            onNavigationItemSelected(it)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_netease -> {
                val fragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_NETEASE)
                        ?: NeteaseMainFragment()
                checkToFragment(fragment, TAG_FRAGMENT_NETEASE)
                return true
            }
            R.id.nav_local -> {
                val fragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_LOCAL)
                        ?: LocalMainFragment()
                checkToFragment(fragment, TAG_FRAGMENT_LOCAL)
                return true
            }
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

    private fun checkToFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.intransaction {
            replace(R.id.container, fragment, tag)
        }
        val toggle = ActionBarDrawerToggle(this, drawerLayout, fragment.getToolbar(),
                R.string.open_navigation_drawer, R.string.close_navigation_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun Fragment.getToolbar(): Toolbar? = null

}
