package tech.summerly.quiet.local

import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.local_activity_main.*
import kotlinx.android.synthetic.main.local_main_header_tab.*
import org.jetbrains.anko.startActivity
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.fragments.BottomControllerFragment
import tech.summerly.quiet.local.fragments.LocalArtistFragment
import tech.summerly.quiet.local.fragments.LocalOverviewFragment
import tech.summerly.quiet.local.fragments.LocalTotalFragment

/**
 * Created by summer on 17-12-21
 */

class LocalMusicActivity : BaseActivity(), BottomControllerFragment.BottomControllerContainer {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.local_activity_main)
        setSupportActionBar(localToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        pager.adapter = SectionsPagerAdapter(supportFragmentManager)
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pager))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.local_menu_activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.local_music_action_scan) {
            startActivity<LocalMusicScannerActivity>()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        val fragments = Array<Fragment?>(count) { null }

        override fun getItem(position: Int): Fragment? = fragments[position] ?: when (position) {
            0 -> LocalOverviewFragment()
            1 -> LocalTotalFragment()
            2 -> LocalArtistFragment()
            3 -> LocalOverviewFragment()
            else -> Fragment()
        }.also {
            fragments[position] = it
        }


        // display four fragment: overview , total , artist , album
        override fun getCount(): Int = 4

    }
}