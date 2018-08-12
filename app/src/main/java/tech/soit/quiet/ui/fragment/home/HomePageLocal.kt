package tech.soit.quiet.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.home_page_local.view.*
import tech.soit.quiet.AppContext
import tech.soit.quiet.R
import tech.soit.quiet.ui.fragment.UnimplementedFragment
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.utils.annotation.LayoutId

/**
 * home page - Local
 *
 * manage local musics
 *
 */
@LayoutId(R.layout.home_page_local)
class HomePageLocal : BaseFragment() {

    companion object {

        /**
         * create an instance
         */
        fun newInstance(): HomePageLocal {
            return HomePageLocal()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = SectionsPagerAdapter(childFragmentManager)
    }


    private class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val fragments = Array<Fragment?>(count) { null }

        override fun getItem(position: Int): Fragment? = fragments[position]
                ?: when (position) {
                    0 -> UnimplementedFragment()
                    1 -> UnimplementedFragment()
                    2 -> UnimplementedFragment()
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