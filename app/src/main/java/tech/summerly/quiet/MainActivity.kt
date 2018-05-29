package tech.summerly.quiet

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.utils.log

/**
 * the main activity of application
 */
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()
        navigationView.menu.findItem(R.id.nav_netease)?.actionView?.findViewById<View>(R.id.indicatorLayout)?.let {
            it.setOnClickListener {
                log { "netease user clicked" }
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

}
