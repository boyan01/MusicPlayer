package tech.soit.quiet.ui.fragment.home

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_main.*
import tech.soit.quiet.R
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.utils.annotation.LayoutId

/**
 * main Fragment
 */
@LayoutId(R.layout.fragment_main)
class MainFragment : BaseFragment(), NavigationView.OnNavigationItemSelectedListener {


    companion object {

        const val TAG = "Main_Fragment"

        /**
         * local music home page
         */
        private const val HOME_PAGE_LOCAL = "home_page_local"

        // list of home page tag
        private val HOME_PAGES = listOf(HOME_PAGE_LOCAL)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationView.setNavigationItemSelectedListener(this)
        checkToFragment(HOME_PAGE_LOCAL)
    }

    /**
     * @param tag [HOME_PAGES]
     */
    private fun checkToFragment(tag: String) {
        val retain = childFragmentManager.findFragmentByTag(tag)
        if (retain != null) {
            childFragmentManager.beginTransaction()
                    .show(retain)
                    .commit()
        } else {
            childFragmentManager.beginTransaction()
                    .replace(R.id.subContent, createHomePage(tag), tag)
                    .commit()
        }
    }

    /**
     * create fragment for this [tag]
     */
    private fun createHomePage(tag: String): BaseFragment {
        return when (tag) {
            HOME_PAGE_LOCAL -> HomePageLocal.newInstance()
            else -> error("illegal tag to create Home Page : $tag")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return false
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}