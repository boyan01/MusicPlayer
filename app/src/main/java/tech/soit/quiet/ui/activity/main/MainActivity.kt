package tech.soit.quiet.ui.activity.main

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import tech.soit.quiet.R
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.ui.fragment.home.HomePageLocal

/**
 * the main activity of application
 */
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {

        /**
         * local music home page
         */
        private const val HOME_PAGE_LOCAL = "home_page_local"


        // list of home page tag
        private val HOME_PAGES = listOf(HOME_PAGE_LOCAL)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationView.setNavigationItemSelectedListener(this)
        checkToFragment(HOME_PAGE_LOCAL)
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

    /**
     * @param tag [HOME_PAGES]
     */
    private fun checkToFragment(tag: String) {
        navigationTo(tag) { createHomePage(tag) }
    }

    private fun createHomePage(tag: String): BaseFragment {
        return when (tag) {
            HOME_PAGE_LOCAL -> HomePageLocal.newInstance()
            else -> error("illegal tag to create Home Page : $tag")
        }
    }

}
