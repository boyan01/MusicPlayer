package tech.summerly.quiet.local

import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.Gravity
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.local_activity_main.*
import kotlinx.android.synthetic.main.local_main_header_tab.*
import org.jetbrains.anko.startActivity
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.fragments.BottomControllerFragment
import tech.summerly.quiet.commonlib.utils.popupMenu
import tech.summerly.quiet.constraints.Local
import tech.summerly.quiet.local.fragments.LocalAlbumFragment
import tech.summerly.quiet.local.fragments.LocalArtistFragment
import tech.summerly.quiet.local.fragments.LocalOverviewFragment
import tech.summerly.quiet.local.fragments.LocalTotalFragment
import tech.summerly.quiet.local.scanner.LocalMusicScannerActivity

/**
 * Created by summer on 17-12-21
 */
@Route(path = Local.PATH_LOCAL_MAIN)
class LocalMusicActivity : BaseActivity(), BottomControllerFragment.BottomControllerContainer {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.local_activity_main)
        pager.adapter = SectionsPagerAdapter(supportFragmentManager)
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pager))
        imageMenu.setOnClickListener {
            popupMenu(it, R.menu.local_menu_activity_main, gravity = Gravity.BOTTOM) {
                val id = it.itemId
                if (id == R.id.local_menu_main_scan) {
                    startActivity<LocalMusicScannerActivity>()
                } else if (id == R.id.local_music_menu_setting) {
                    ARouter.getInstance().build("/setting/main").navigation()
                }
                true
            }
        }
    }

    fun setCurrentPage(position: Int, smoothScroll: Boolean = true) {
        pager.setCurrentItem(position, smoothScroll)
    }


    private inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        val fragments = Array<Fragment?>(count) { null }

        override fun getItem(position: Int): Fragment? = fragments[position] ?: when (position) {
            0 -> LocalOverviewFragment()
            1 -> LocalTotalFragment()
            2 -> LocalArtistFragment()
            3 -> LocalAlbumFragment()
            else -> Fragment()
        }.also {
            fragments[position] = it
        }


        // display four fragment: overview , total , artist , album
        override fun getCount(): Int = 4

    }
}