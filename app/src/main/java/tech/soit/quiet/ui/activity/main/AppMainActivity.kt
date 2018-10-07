package tech.soit.quiet.ui.activity.main

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_app_main.*
import kotlinx.android.synthetic.main.main_content.*
import tech.soit.quiet.R
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.fragment.home.MainCloudFragment
import tech.soit.quiet.ui.fragment.home.MainMusicFragment
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.support.attrValue

/**
 * the main activity of application
 */
@LayoutId(R.layout.activity_app_main)
class AppMainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init drawer toggle
        val drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_navigation_drawer, R.string.close_navigation_drawer)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.drawerArrowDrawable.color = attrValue(R.attr.quietTextPrimaryInverse)
        drawerToggle.syncState()

        //contract pager with tab layout
        pagerMain.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pagerMain))
        pagerMain.adapter = MainPagerAdapter()

        //inflate menu to toolbar, only has ONE option which is SEARCH
        toolbar.inflateMenu(R.menu.menu_app_main)

        listenBottomControllerEvent()
    }


    inner class MainPagerAdapter : FragmentPagerAdapter(supportFragmentManager) {

        private val _fragments = Array<Fragment?>(2) { null }

        override fun getItem(position: Int): Fragment {
            if (_fragments[position] == null) {
                _fragments[position] = when (position) {
                    0 -> MainMusicFragment()
                    1 -> MainCloudFragment()
                    else -> error("can not create page for MainPager , position : $position")
                }
            }
            return _fragments[position]!!
        }

        override fun getCount(): Int {
            return 2
        }

    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}