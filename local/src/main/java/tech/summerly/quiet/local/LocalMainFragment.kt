package tech.summerly.quiet.local

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.local_fragment_main.*
import kotlinx.android.synthetic.main.local_fragment_main.view.*
import kotlinx.android.synthetic.main.local_main_header_tab.view.*
import org.jetbrains.anko.startActivity
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.utils.popupMenu
import tech.summerly.quiet.constraints.Local
import tech.summerly.quiet.constraints.Setting
import tech.summerly.quiet.local.fragments.LocalAlbumFragment
import tech.summerly.quiet.local.fragments.LocalArtistFragment
import tech.summerly.quiet.local.fragments.MusicListFragment
import tech.summerly.quiet.local.scanner.LocalMusicScannerActivity

@Route(path = Local.FRAGMENT_LOCAL_MAIN)
class LocalMainFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.local_fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        pager.adapter = SectionsPagerAdapter(childFragmentManager)
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pager))
        imageMenu.setOnClickListener {
            popupMenu(it, R.menu.local_menu_activity_main, gravity = Gravity.BOTTOM) {
                val id = it.itemId
                if (id == R.id.local_menu_main_scan) {
                    startActivity<LocalMusicScannerActivity>()
                } else if (id == R.id.local_music_menu_setting) {
                    ARouter.getInstance().build(Setting.ACTIVITY_SETTING_MAIN).navigation()
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

        override fun getItem(position: Int): Fragment? = fragments[position]
                ?: when (position) {
                    0 -> MusicListFragment()
                    1 -> LocalArtistFragment()
                    2 -> LocalAlbumFragment()
                    else -> Fragment()
                }.also {
                    fragments[position] = it
                }


        // display four fragment: overview , total , artist , album
        override fun getCount(): Int = 3

    }
}