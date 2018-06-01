package tech.summerly.quiet

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_main.*
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.constraints.Setting
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

    private var checkedFragment = TAG_FRAGMENT_NETEASE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_netease)

        //拦截NavigationView读取和消耗 WindowsInsets，已向Google报告Bug，以后再
        //更改逻辑以适配底部控制栏。
        navigationView.setOnApplyWindowInsetsListener(null)
    }

    override fun onStart() {
        super.onStart()
        checkToFragment(checkedFragment)
        navigationView.menu.findItem(R.id.nav_netease)?.actionView?.findViewById<View>(R.id.indicatorLayout)?.let {
            it.setOnClickListener {

            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_netease -> {
                checkToFragment(TAG_FRAGMENT_NETEASE)
                return true
            }
            R.id.nav_local -> {
                checkToFragment(TAG_FRAGMENT_LOCAL)
                return true
            }
            R.id.nav_settings -> {
                ARouter.getInstance().build(Setting.ACTIVITY_SETTING_MAIN).navigation()
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

    private fun checkToFragment(tag: String) {
        checkedFragment = tag
        val fragment = (supportFragmentManager.findFragmentByTag(tag) ?: when (tag) {
            TAG_FRAGMENT_NETEASE -> NeteaseMainFragment()
            TAG_FRAGMENT_LOCAL -> LocalMainFragment()
            else -> error("error tag checked")
        }) as Fragment
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment, tag)
                .commitNow()
        val toolbar = fragment.toolbar()
        if (toolbar == null) {
            log { "toolbar == null" }
        }
        val toggle = getToggle(fragment.toolbar())
        toggle.syncState()
        drawerLayout.closeDrawer(GravityCompat.START)
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
        is NeteaseMainFragment -> getToolbar()
        is LocalMainFragment -> getToolbar()
        else -> null
    }

}
