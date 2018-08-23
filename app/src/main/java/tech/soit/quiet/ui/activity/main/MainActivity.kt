package tech.soit.quiet.ui.activity.main

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringDef
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import tech.soit.quiet.R
import tech.soit.quiet.ui.activity.base.BaseActivity
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


        @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
        @StringDef(HOME_PAGE_LOCAL)
        @Retention(AnnotationRetention.SOURCE)
        annotation class HomePage

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

    private fun checkToFragment(@HomePage tag: String) {

        val transaction = supportFragmentManager.beginTransaction()

        //hide home pages
        HOME_PAGES
                .mapNotNull {
                    supportFragmentManager.findFragmentByTag(it)
                }.filter {
                    !it.isHidden
                }.forEach {
                    transaction.hide(it)
                }

        val fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment != null) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.container, createHomePage(tag), tag)
        }

        transaction.commit()
    }

    private fun createHomePage(@HomePage tag: String): Fragment {
        return when (tag) {
            HOME_PAGE_LOCAL -> HomePageLocal.newInstance()
            else -> error("illegal tag to create Home Page : $tag")
        }
    }

}
