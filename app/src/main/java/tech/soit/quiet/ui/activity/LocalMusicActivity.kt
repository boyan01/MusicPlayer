package tech.soit.quiet.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_local_music.*
import tech.soit.quiet.AppContext
import tech.soit.quiet.R
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.fragment.local.LocalAlbumFragment
import tech.soit.quiet.ui.fragment.local.LocalArtistFragment
import tech.soit.quiet.ui.fragment.local.LocalSingleSongFragment
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.log

@LayoutId(R.layout.activity_local_music)
class LocalMusicActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = LocalPagerAdapter()

        //init toolbar
        toolbar.inflateMenu(R.menu.menu_local_home_page)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.menu_local_home_scan) {
                log { "to scanner" }
            }
            true
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private inner class LocalPagerAdapter : FragmentPagerAdapter(supportFragmentManager) {

        private val fragments = Array<Fragment?>(count) { null }

        override fun getItem(position: Int): Fragment? = fragments[position]
                ?: when (position) {
                    0 -> LocalSingleSongFragment()
                    1 -> LocalArtistFragment()
                    2 -> LocalAlbumFragment()
                    else -> error("illegal position $position")
                }.also {
                    fragments[position] = it
                }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> AppContext.getString(R.string.single_song)
                1 -> AppContext.getString(R.string.artist)
                2 -> AppContext.getString(R.string.album)
                else -> null
            }
        }


        /**
         * display three fragment: single song , artist , album
         */
        override fun getCount(): Int = 3

    }

}